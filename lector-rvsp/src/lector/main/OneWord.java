package lector.main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

public class OneWord extends JPanel {

	private static final long serialVersionUID = 3568780586785687917L;
	private int orp, posBeforeOrp, posOrp, posAfterOrp, baseLine;
	private float lineWidth = 2f, markerPos = 0.3f;
	private String word, strBeforeOrp, strOrp, strAfterOrp;
	private Font font = new Font("SansSerif", Font.PLAIN, 20);
	private Color txtColor = Color.BLACK;
	private Color orpColor = Color.RED;
	private Color lineColor = Color.BLACK;
	private boolean calculatedPos = false;

	private int getORP(String str) {
		final int[] table = {0, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4};
		int length = str.length();
		
		if(length < 1) return 0;
		if(length > 13) return 5;
		return table[length];
	}
	
	private void calcPositions(Graphics2D g2) {
		FontMetrics fm = g2.getFontMetrics();
		Rectangle2D rectOrp = fm.getStringBounds(strOrp, g2);
		posOrp = (int) (getWidth()*markerPos - rectOrp.getWidth()/2);
		posBeforeOrp = (int) (posOrp - fm.getStringBounds(strBeforeOrp, g2).getWidth());
		posAfterOrp = (int) (posOrp + rectOrp.getWidth());
		baseLine = (int) (getHeight()/2 + fm.getAscent()/2);
	}
	
	public OneWord() {
		setBackground(Color.WHITE);
	}
	
	public void setWord(String _word) {
		word = _word;
		orp = getORP(_word);
		
		strBeforeOrp = word.substring(0, orp);
		strOrp = word.substring(orp, orp+1);
		strAfterOrp = word.substring(orp+1);
		calculatedPos = false;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		
		if(word != null) {
			g2.setRenderingHint(
			        RenderingHints.KEY_TEXT_ANTIALIASING,
			        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2.setFont(font);
			g2.setColor(txtColor);

			if(!calculatedPos) {
				calcPositions(g2);
				calculatedPos = true;
			}

			g2.setColor(orpColor);
			g2.drawString(strOrp, posOrp, baseLine);
			
			g2.setColor(txtColor);
			g2.drawString(strBeforeOrp, posBeforeOrp, baseLine);
			g2.drawString(strAfterOrp, posAfterOrp, baseLine);
		}

		g2.setColor(lineColor);
		float halfLine = lineWidth/2;
		Rectangle2D box = new Rectangle2D.Float(halfLine, halfLine, getWidth()-lineWidth, getHeight()-lineWidth);
		g2.setStroke(new BasicStroke(lineWidth));
		g2.draw(box);
		Line2D marker = new Line2D.Float(0f, 0f, 0f, getHeight()*0.2f);
		g2.translate(getWidth()*markerPos, 0);
		g2.draw(marker);
		g2.translate(0, getHeight()*0.8f);
		g2.draw(marker);
	}
}