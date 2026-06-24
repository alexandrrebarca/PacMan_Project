import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.RenderingHints;


public class MenuScreen {

    private final int boardWidth;
    private final int boardHeight;

    public MenuScreen(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
    }


    public void draw(Graphics g, double menuPacX, int menuMouthAngle, int menuAnimTimer,
                     Skin[] skins, int selectedSkin,
                     String[][] mazeMaps, String[] mazeNames, int selectedMap) {

        if (g instanceof Graphics2D) {
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }


        if (g instanceof Graphics2D) {
            Graphics2D g2 = (Graphics2D) g;
            GradientPaint bg = new GradientPaint(0, 0, new Color(10, 10, 28), 0, boardHeight, new Color(0, 0, 0));
            g2.setPaint(bg);
            g2.fillRect(0, 0, boardWidth, boardHeight);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, boardWidth, boardHeight);
        }

        Font titleFont   = new Font("Verdana", Font.BOLD, 34);
        Font headerFont  = new Font("Verdana", Font.BOLD, 15);
        Font nameFont    = new Font("Verdana", Font.BOLD, 17);
        Font hintFont    = new Font("Verdana", Font.PLAIN, 11);
        Font promptFont  = new Font("Verdana", Font.BOLD, 16);
        Color gold = new Color(255, 200, 40);
        Color dim = new Color(140, 140, 150);


        g.setFont(titleFont);
        String title = "PAC-MAN";
        int titleWidth = g.getFontMetrics().stringWidth(title);
        g.setColor(new Color(0, 0, 0));
        g.drawString(title, (boardWidth - titleWidth) / 2 + 2, 58);
        g.setColor(gold);
        g.drawString(title, (boardWidth - titleWidth) / 2, 56);


        g.setColor(new Color(60, 60, 70));
        for (int dx = 10; dx < boardWidth - 10; dx += 16) {
            g.fillOval(dx, 84, 3, 3);
        }
        PacmanRenderer.draw(g, (int) menuPacX, 70, 26, 26, 'R', skins[selectedSkin].color, menuMouthAngle);


        int panelTop = 110;
        int panelHeight = 280;
        int panelGap = 14;
        int panelWidth = (boardWidth - panelGap * 3) / 2;
        int leftX = panelGap;
        int rightX = panelGap * 2 + panelWidth;

        drawPanelFrame(g, leftX, panelTop, panelWidth, panelHeight);
        drawPanelFrame(g, rightX, panelTop, panelWidth, panelHeight);


        int cx = leftX + panelWidth / 2;
        g.setFont(headerFont);
        g.setColor(dim);
        String charLabel = "CHARACTER";
        g.drawString(charLabel, cx - g.getFontMetrics().stringWidth(charLabel) / 2, panelTop + 24);

        int previewSize = 64;
        int previewY = panelTop + 44;
        int previewX = cx - previewSize / 2;
        PacmanRenderer.draw(g, previewX, previewY, previewSize, previewSize, 'R', skins[selectedSkin].color, menuMouthAngle);

        g.setFont(new Font("Verdana", Font.BOLD, 20));
        g.setColor(gold);
        g.drawString("<", leftX + 14, previewY + previewSize / 2 + 8);
        g.drawString(">", leftX + panelWidth - 26, previewY + previewSize / 2 + 8);

        g.setFont(nameFont);
        g.setColor(Color.WHITE);
        String skinName = skins[selectedSkin].name;
        g.drawString(skinName, cx - g.getFontMetrics().stringWidth(skinName) / 2, previewY + previewSize + 30);

        int dotsY = previewY + previewSize + 50;
        drawSelectorDots(g, cx, dotsY, skins.length, selectedSkin, true);

        g.setFont(hintFont);
        g.setColor(dim);
        String charHint = "← LEFT / RIGHT →";
        g.drawString(charHint, cx - g.getFontMetrics().stringWidth(charHint) / 2, panelTop + panelHeight - 16);


        int mx = rightX + panelWidth / 2;
        g.setFont(headerFont);
        g.setColor(dim);
        String mapLabel = "MAP";
        g.drawString(mapLabel, mx - g.getFontMetrics().stringWidth(mapLabel) / 2, panelTop + 24);


        int iconW = 64;
        int iconH = 76;
        int iconY = panelTop + 44;
        int iconX = mx - iconW / 2;
        drawMapIcon(g, iconX, iconY, iconW, iconH, mazeMaps[selectedMap]);

        g.setFont(nameFont);
        g.setColor(Color.WHITE);
        String mapName = mazeNames[selectedMap];
        g.drawString(mapName, mx - g.getFontMetrics().stringWidth(mapName) / 2, iconY + iconH + 30);

        int mapDotsY = iconY + iconH + 50;
        drawSelectorDots(g, mx, mapDotsY, mazeMaps.length, selectedMap, false);

        g.setFont(hintFont);
        g.setColor(dim);
        String mapHint = "↑ UP / DOWN ↓";
        g.drawString(mapHint, mx - g.getFontMetrics().stringWidth(mapHint) / 2, panelTop + panelHeight - 16);


        g.setFont(promptFont);
        int pulse = (menuAnimTimer / 10) % 2;
        g.setColor(pulse == 0 ? Color.WHITE : dim);
        String prompt = "PRESS ENTER TO START";
        int promptWidth = g.getFontMetrics().stringWidth(prompt);
        g.drawString(prompt, (boardWidth - promptWidth) / 2, panelTop + panelHeight + 36);
    }


    private void drawPanelFrame(Graphics g, int x, int y, int w, int h) {
        g.setColor(new Color(18, 18, 36));
        g.fillRoundRect(x, y, w, h, 16, 16);
        g.setColor(new Color(80, 80, 130));
        g.drawRoundRect(x, y, w, h, 16, 16);
    }


    private void drawSelectorDots(Graphics g, int cx, int y, int count, int selected, boolean round) {
        int spacing = 16;
        int startX = cx - (count - 1) * spacing / 2;
        for (int i = 0; i < count; i++) {
            g.setColor(i == selected ? Color.WHITE : new Color(70, 70, 85));
            int dx = startX + i * spacing - 3;
            if (round) g.fillOval(dx, y - 3, 6, 6);
            else g.fillRect(dx, y - 3, 6, 6);
        }
    }



    private void drawMapIcon(Graphics g, int x, int y, int w, int h, String[] map) {
        g.setColor(new Color(8, 8, 16));
        g.fillRoundRect(x, y, w, h, 8, 8);

        int rows = map.length;
        int cols = map[0].length();
        double cellW = (double) w / cols;
        double cellH = (double) h / rows;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char ch = map[r].charAt(c);
                Color color;
                if (ch == 'X') {
                    color = new Color(70, 110, 230);
                } else if (ch == 'H' || ch == 'G') {
                    color = new Color(150, 90, 210);
                } else if (ch == 'P') {
                    color = new Color(255, 210, 0);
                } else if (ch == 'b' || ch == 'p' || ch == 'o' || ch == 'r') {
                    color = new Color(220, 60, 60);
                } else if (ch == ' ') {
                    color = new Color(60, 60, 80);
                } else {
                    continue;
                }
                g.setColor(color);
                int px = x + (int) Math.round(c * cellW);
                int py = y + (int) Math.round(r * cellH);
                int pw = Math.max(1, (int) Math.ceil(cellW));
                int ph = Math.max(1, (int) Math.ceil(cellH));
                g.fillRect(px, py, pw, ph);
            }
        }

        g.setColor(new Color(80, 80, 130));
        g.drawRoundRect(x, y, w, h, 8, 8);
    }
}
