public class NBody {
	public static double readRadius(String path) {
		In in = new In(path);
		int PlanetsNum = in.readInt();
		double Radius = in.readDouble();
		return Radius;		
	}

	public static Planet[] readPlanets(String path) {
		In in = new In(path);
		int PlanetsNum = in.readInt();
		double Radius = in.readDouble();
		Planet[] planets = new Planet[PlanetsNum];
		int i = 0;
		while (i<PlanetsNum) {
			double xP = in.readDouble();
			double yP = in.readDouble();
			double xV = in.readDouble();
			double yV = in.readDouble();
			double m = in.readDouble();
			String img = in.readString();
			planets[i] = new Planet(xP,yP,xV,yV,m,img);
			i = i + 1;
		}
		return planets;
	} 

	public static void main(String[] args) {
		StdDraw.enableDoubleBuffering();
		double T = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		String filename = args[2];
		double Radius = readRadius(filename);
		Planet[] planets = readPlanets(filename);

		StdDraw.setScale(-Radius,Radius);
		StdDraw.picture(0, 0, "images/starfield.jpg");
		
		for (Planet p : planets) {
			p.draw();
		}
		StdDraw.show();
		StdDraw.pause(10);

		
		double time = 0.0;
		double[] xForces = new double[planets.length];
		double[] yForces = new double[planets.length];
		while (time<T) {
			int i = 0;
			while (i<planets.length) {
				xForces[i] = planets[i].calcNetForceExertedByX(planets);
				yForces[i] = planets[i].calcNetForceExertedByY(planets);
				i = i+1;
			}

			i = 0;
			while (i<planets.length) {
				planets[i].update(dt,xForces[i],yForces[i]);
				i = i+1;				
			}
			StdDraw.picture(0, 0, "images/starfield.jpg");
			for (Planet p : planets) {
				p.draw();
			}
			StdDraw.show();
			StdDraw.pause(10);
			time = time+dt;
		}
	}
}