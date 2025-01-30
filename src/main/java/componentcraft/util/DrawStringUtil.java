package componentcraft.util;

import java.awt.*;

public class DrawStringUtil {


    /**
     *
     * @param g  {@code Graphics2D }. Graphic2D on which the string is drawn
     * @param text {@code String}. String which drawn on Graphics2D
     * @param x {@code int}. Integer x-coordinate
     * @param y {@code int}. Integer y-coordinate
     * @param maxWidth {@code int}. Integer which is max width of in pixel of the string.
     * @param inMiddle {@code boolean}. Boolean which says if the string shout draw in the x-mid or not
     */
    public static void drawStringWithMaxWidth(Graphics2D g, String text, int x, int y, int maxWidth, boolean inMiddle) {
        FontMetrics fontMetrics = g.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(text);

//        if(Logger.debugGraphics){
//            g.drawRect( x + maxWidth / 2  - textWidth /2 , y - g.getFont().getSize(), textWidth, g.getFont().getSize());
//        }

        if (textWidth > maxWidth) {
            String ellipsis = "...";
            int ellipsisWidth = fontMetrics.stringWidth(ellipsis);

            int availableWidth = maxWidth - ellipsisWidth - fontMetrics.stringWidth(ellipsis);
            int charIndex = 0;

            while (charIndex < text.length() && fontMetrics.stringWidth(text.substring(0, charIndex + 1)) <= availableWidth) {
                charIndex++;
            }

            text = text.substring(0, charIndex) + ellipsis;
        }else{
            if(inMiddle){
                if(textWidth/2 + maxWidth / 2 < maxWidth){
                    x = x + maxWidth / 2  - textWidth /2 ;
                }
            }
        }
        g.drawString(text, x, y);
    }
}
