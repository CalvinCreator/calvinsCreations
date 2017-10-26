package bot;

public class Bullet {
	
	private int x, y;
	private float speed;
	private double angle;
	
	public Bullet(int x, int y, double angle) {
		this.x = x; this.y = y;
		this.angle = angle;
		speed = 3;
	}
	
	public void update() {
		x += speed * Math.cos(angle);
		y += speed * Math.sin(angle);
	}
	
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public double getAngle() {
		return angle;
	}

}
