package com.ethanpunter.insulincalculator;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RatioReader {

    private static final String TAG = RatioReader.class.getSimpleName();

    public static List<TimedRatio> readRatioFile(AssetManager assets, String fileName) {
        List<TimedRatio> ratios = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(assets.open(fileName)))) {

            String mLine;
            while ((mLine = reader.readLine()) != null) {
                String[] line = mLine.split(",");
                ratios.add(new TimedRatio(
                        Integer.parseInt(line[0]), // From
                        Integer.parseInt(line[1]), // To
                        Float.parseFloat(line[2]))); // Ratio
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to open ratio file");
        } catch (NumberFormatException e) {
            Log.e(TAG, "File contained invalid values");
        }

        if (ratiosHaveValidRange(ratios)) {
            return ratios;
        }
        return new ArrayList<>();
    }

    private static boolean ratiosHaveValidRange(List<TimedRatio> ratios) {
        for (int i = 0; i < ratios.size(); i++) {
            if (i == 0) {
                if (ratios.get(i).getFrom() != 0) {
                    return false;
                }
            } else {
                if (ratios.get(i).getFrom() != ratios.get(i - 1).getTo()) {
                    return false;
                }
            }
            if (i == ratios.size() - 1 && ratios.get(i).getTo() != 24) {
                return false;
            }
        }
        return true;
    }
}
