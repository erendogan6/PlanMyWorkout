package com.erendogan6.planmyworkout.feature.onboarding.service;

import android.util.Log;

import com.erendogan6.planmyworkout.core.util.ConfigManager;
import com.erendogan6.planmyworkout.feature.onboarding.model.AiWorkoutPlanRequest;
import com.erendogan6.planmyworkout.feature.onboarding.model.AiWorkoutPlanResponse;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AiWorkoutPlanService {

    private static final String TAG = "AiWorkoutPlanService";
    private static final String MODEL_NAME = "gemini-2.0-flash";
    private final ConfigManager configManager;
    private final GenerativeModelFutures model;

    @Inject
    public AiWorkoutPlanService(ConfigManager configManager) {
        this.configManager = configManager;

        if (!configManager.isGeminiApiKeyAvailable()) {
            Log.e(TAG, "Gemini API key not available from remote config!");
            throw new IllegalStateException("Gemini API key not configured");
        }

        String apiKey = configManager.getGeminiApiKey();

        GenerativeModel gm = new GenerativeModel(MODEL_NAME, apiKey);
        this.model = GenerativeModelFutures.from(gm);
    }

    public Task<AiWorkoutPlanResponse> generateWorkoutPlan(AiWorkoutPlanRequest request) {
        TaskCompletionSource<AiWorkoutPlanResponse> taskSource = new TaskCompletionSource<>();

        try {
            String prompt = createStructuredPrompt(request);
            Log.d(TAG, "Sending structured prompt to AI");

            Content content = new Content.Builder().addText(prompt).build();

            ListenableFuture<GenerateContentResponse> future = model.generateContent(content);

            Futures.addCallback(future, new FutureCallback<>() {
                @Override
                public void onSuccess(GenerateContentResponse response) {
                    try {
                        Log.d(TAG, "Received response from AI");
                        AiWorkoutPlanResponse result = parseStructuredResponse(response.getText(), request);
                        taskSource.setResult(result);
                    } catch (Exception e) {
                        Log.e(TAG, "Failed to parse structured response", e);
                        taskSource.setException(new RuntimeException("Failed to parse AI response: " + e.getMessage(), e));
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e(TAG, "AI generation failed", t);
                    taskSource.setException(new RuntimeException("Failed to generate workout plan: " + t.getMessage(), t));
                }
            }, MoreExecutors.directExecutor());

        } catch (Exception e) {
            Log.e(TAG, "Failed to create request", e);
            taskSource.setException(new RuntimeException("Failed to create request: " + e.getMessage(), e));
        }

        return taskSource.getTask();
    }

    private String createStructuredPrompt(AiWorkoutPlanRequest request) {
        return String.format(
                "Create a personalized workout plan based on these requirements:\n\n" +
                        "Goal: %s\n" +
                        "Experience: %s\n" +
                        "Days per week: %d\n" +
                        "Session duration: %d minutes\n" +
                        "Equipment: %s\n" +
                        "Limitations: %s\n" +
                        "Focus areas: %s\n\n" +

                        "Respond in this EXACT format (no deviations):\n\n" +
                        "PLAN_NAME: [Your plan name here]\n" +
                        "DESCRIPTION: [Brief description of the plan]\n" +
                        "DIFFICULTY: [Beginner/Intermediate/Advanced]\n\n" +
                        "EXERCISES:\n" +
                        "1. [Exercise Name] | [Muscle Group] | [Brief description and form tips]\n" +
                        "2. [Exercise Name] | [Muscle Group] | [Brief description and form tips]\n" +
                        "3. [Exercise Name] | [Muscle Group] | [Brief description and form tips]\n" +
                        "4. [Exercise Name] | [Muscle Group] | [Brief description and form tips]\n" +
                        "5. [Exercise Name] | [Muscle Group] | [Brief description and form tips]\n\n" +

                        "RULES:\n" +
                        "- Create exactly 5-7 exercises\n" +
                        "- Use pipe (|) separators between fields\n" +
                        "- Match equipment available\n" +
                        "- Consider limitations mentioned\n" +
                        "- Focus on exercise selection, not sets/reps\n" +
                        "- No markdown, no extra formatting\n\n" +

                        "EXAMPLE:\n" +
                        "1. Push-ups | Chest | Classic bodyweight exercise targeting chest, shoulders and triceps\n",

                request.getGoal(),
                request.getExperienceLevel(),
                request.getDaysPerWeek(),
                request.getSessionDuration(),
                request.getEquipment().isEmpty() ? "Bodyweight only" : request.getEquipment(),
                request.getLimitations().isEmpty() ? "None" : request.getLimitations(),
                request.getFocusAreas().isEmpty() ? "Full body" : request.getFocusAreas()
        );
    }

    private AiWorkoutPlanResponse parseStructuredResponse(String text, AiWorkoutPlanRequest request) {
        try {
            Log.d(TAG, "Raw AI response:\n" + text);

            AiWorkoutPlanResponse response = new AiWorkoutPlanResponse();
            List<AiWorkoutPlanResponse.AiExercise> exercises = new ArrayList<>();

            String[] lines = text.split("\n");

            for (String line : lines) {
                line = line.trim();

                if (line.startsWith("PLAN_NAME:")) {
                    String name = line.substring(10).trim();
                    response.setName(name.isEmpty() ? "AI Generated Workout Plan" : name);

                } else if (line.startsWith("DESCRIPTION:")) {
                    String desc = line.substring(12).trim();
                    response.setDescription(desc.isEmpty() ? "Personalized workout plan created by AI" : desc);

                } else if (line.startsWith("DIFFICULTY:")) {
                    String difficulty = line.substring(11).trim();
                    response.setDifficulty(validateDifficulty(difficulty));

                } else if (line.matches("^\\d+\\..*") && line.contains("|")) {
                    // Parse exercise line: "1. Push-ups | Chest | 3 | 12 | 60 | Description"
                    AiWorkoutPlanResponse.AiExercise exercise = parseExerciseLine(line);
                    if (exercise != null) {
                        exercises.add(exercise);
                    }
                }
            }

            // Set defaults if not parsed
            if (response.getName() == null) {
                response.setName("AI Generated Workout Plan");
            }
            if (response.getDescription() == null) {
                response.setDescription("Personalized workout plan created by AI");
            }
            if (response.getDifficulty() == null) {
                response.setDifficulty(mapExperienceLevel(request.getExperienceLevel()));
            }

            response.setDays(request.getDaysPerWeek());
            response.setDurationWeeks(4);
            response.setExercises(exercises);

            // Validate result
            if (exercises.isEmpty()) {
                throw new RuntimeException("No exercises found in AI response");
            }

            Log.d(TAG, "Successfully parsed " + exercises.size() + " exercises");
            return response;

        } catch (Exception e) {
            Log.e(TAG, "Error parsing structured response", e);
            throw new RuntimeException("Failed to parse AI response: " + e.getMessage(), e);
        }
    }

    private AiWorkoutPlanResponse.AiExercise parseExerciseLine(String line) {
        try {
            String content = line.replaceFirst("^\\d+\\.\\s*", "");
            String[] parts = content.split("\\|");

            if (parts.length < 3) {
                Log.w(TAG, "Invalid exercise line format: " + line);
                return null;
            }

            AiWorkoutPlanResponse.AiExercise exercise = new AiWorkoutPlanResponse.AiExercise();
            exercise.setName(parts[0].trim());
            exercise.setMuscleGroup(parts[1].trim());
            exercise.setDescription(parts[2].trim());

            // Validate parsed exercise
            if (exercise.getName().isEmpty() || exercise.getMuscleGroup().isEmpty()) {
                Log.w(TAG, "Exercise missing required fields: " + line);
                return null;
            }

            return exercise;

        } catch (Exception e) {
            Log.w(TAG, "Failed to parse exercise line: " + line, e);
            return null;
        }
    }

    private String validateDifficulty(String difficulty) {
        if (difficulty == null) return "Intermediate";

        String clean = difficulty.toLowerCase().trim();
        if (clean.contains("beginner")) return "Beginner";
        if (clean.contains("advanced")) return "Advanced";
        return "Intermediate";
    }

    private String mapExperienceLevel(String experienceLevel) {
        if (experienceLevel == null) return "Intermediate";

        String clean = experienceLevel.toLowerCase();
        if (clean.contains("beginner") || clean.contains("🌱")) return "Beginner";
        if (clean.contains("advanced") || clean.contains("🏆")) return "Advanced";
        return "Intermediate";
    }
}