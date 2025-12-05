package com.codefork.aoc2025.day05;

import com.codefork.aoc2025.Problem;

public class Part01 extends Problem {

    @Override
    public String solve() {
        var db = Inventory.readDatabase(getInput());
        return String.valueOf(db.getNumFreshIngredients());
    }

    public static void main(String[] args) {
        new Part01().run();
    }
}
