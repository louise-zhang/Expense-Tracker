package com.example.expensetracker;

import android.graphics.Color;

import java.util.Random;

public class RandomColor {
    /**
     * 随机生成颜色
     *
     * @return 返回红绿蓝三原色所能调成的任意一种颜色
     */
    public static int getRandomColor() {
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return Color.rgb(r, g, b);
    }
}
