import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.marker.AbstractMarker;
import de.fhpotsdam.unfolding.geo.Location;


public class MarkerBubble extends AbstractMarker {
    App app;
    float width;
    float height;
    float[] fillColor;
    public static final float[] DEFAULT_FILL = {255, 0, 0, 127f}; 

    public MarkerBubble(App app, Location location, float radius) {
        this(app, location, radius, DEFAULT_FILL);
    }

    public MarkerBubble(App app, Location location, float radius, float[] fillColor) {
        super(location);
        this.app = app;
        this.width = 2*radius;
        this.height = 2*radius;
        this.fillColor = fillColor;
    }

    @Override
    public void draw(PGraphics pg, float x, float y) {
        float zoom = this.app.map.getZoomLevel();
        pg.pushStyle();
		pg.noStroke(); 
        pg.ellipseMode(PConstants.CENTER); 
        pg.fill(this.fillColor[0], this.fillColor[1], this.fillColor[2], this.fillColor[3]);
        pg.ellipse(x, y, zoom*this.width, zoom*this.height);
        pg.popStyle();
    }

    @Override
    protected boolean isInside(float checkX, float checkY, float x, float y) {
        return checkX > x && checkX < x + this.width && checkY > y && checkY < y + this.height;
    }

} 