package xyz.larkyy.colorutils;

import net.md_5.bungee.api.ChatColor;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Colors {

    public static List<String> format(List<String> str) {
        return str.stream().map(Colors::format).collect(Collectors.toList());
    }

    public static String format(String str) {
        return str == null ? null : process(str);
    }

    private static final Map<Color, ChatColor> colors = new HashMap<>();
    private static final List<IPattern> patterns = Arrays.asList(new GradientPattern(), new ClassicPattern());
    private static final boolean SUPPORTSRGB = true;

    private static net.md_5.bungee.api.ChatColor getClosestColor(java.awt.Color color) {
        java.awt.Color nearestColor = null;
        double nearestDistance = 2.147483647E9D;

        for (Color constantColor : colors.keySet()) {
            double distance = Math.pow((double) (color.getRed() - constantColor.getRed()), 2.0D) + Math.pow((double) (color.getGreen() - constantColor.getGreen()), 2.0D) + Math.pow((double) (color.getBlue() - constantColor.getBlue()), 2.0D);
            if (nearestDistance > distance) {
                nearestColor = constantColor;
                nearestDistance = distance;
            }
        }

        return colors.get(nearestColor);
    }

    public static String process(String string) {
        string = net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', string);

        IPattern pattern;
        for(Iterator<IPattern> var1 = patterns.iterator(); var1.hasNext(); string = pattern.process(string)) {
            pattern = var1.next();
        }

        return string;
    }

    public static String color(String string, Color start, Color end, String color) {
        StringBuilder stringBuilder = new StringBuilder();
        net.md_5.bungee.api.ChatColor[] colors = createGradient(start, end, string.length());
        String[] characters = string.split("");

        for(int i = 0; i < string.length(); ++i) {
            stringBuilder.append(colors[i]).append(color).append(characters[i]);
        }

        return stringBuilder.toString();
    }

    public static net.md_5.bungee.api.ChatColor getColor(String string) {
        return SUPPORTSRGB ? net.md_5.bungee.api.ChatColor.of(new Color(Integer.parseInt(string, 16))) : getClosestColor(new Color(Integer.parseInt(string, 16)));
    }

    private static net.md_5.bungee.api.ChatColor[] createGradient(Color start, Color end, int step) {
        net.md_5.bungee.api.ChatColor[] colors = new net.md_5.bungee.api.ChatColor[step];
        int stepR = Math.abs(start.getRed() - end.getRed()) / (step - 1);
        int stepG = Math.abs(start.getGreen() - end.getGreen()) / (step - 1);
        int stepB = Math.abs(start.getBlue() - end.getBlue()) / (step - 1);
        int[] direction = new int[]{start.getRed() < end.getRed() ? 1 : -1, start.getGreen() < end.getGreen() ? 1 : -1, start.getBlue() < end.getBlue() ? 1 : -1};

        for(int i = 0; i < step; ++i) {
            Color color = new Color(start.getRed() + stepR * i * direction[0], start.getGreen() + stepG * i * direction[1], start.getBlue() + stepB * i * direction[2]);
            if (SUPPORTSRGB) {
                colors[i] = net.md_5.bungee.api.ChatColor.of(color);
            } else {
                colors[i] = getClosestColor(color);
            }
        }

        return colors;
    }
}
