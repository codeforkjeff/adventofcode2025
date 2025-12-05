package com.codefork.aoc2025.day05;

import com.codefork.aoc2025.Problem;
import com.codefork.aoc2025.util.Assert;

public class Part02 extends Problem {

    @Override
    public String solve() {
        var sampleDb = Inventory.readDatabase(getSampleInput());
        Assert.assertEquals("14", String.valueOf(sampleDb.getNumFreshIngredientIDs()));

        var db = Inventory.readDatabase(getInput());
        return String.valueOf(db.getNumFreshIngredientIDs());
    }

    public static void main(String[] args) {
        new Part02().run();
    }
}
