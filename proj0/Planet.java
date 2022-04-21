public class Planet {
	public double xxPos;
	public double yyPos;
	public double xxVel;
	public double yyVel;
	public double mass;
	public String imgFileName;

	public Planet(double xP, double yP, double xV,
              double yV, double m, String img) {
		this.xxPos = xP;
		this.yyPos = yP;
		this.xxVel = xV;
		this.yyVel = yV;
		this.mass = m;
		this.imgFileName = img;
	}

	public Planet(Planet p) {
		this.xxPos = p.xxPos;
		this.yyPos = p.yyPos;
		this.xxVel = p.xxVel;
		this.yyVel = p.yyVel;
		this.mass = p.mass;
		this.imgFileName = p.imgFileName;
	}

	public double calcDistance(Planet p) {
		double dx = p.xxPos - this.xxPos;
		double dy = p.yyPos - this.yyPos;
		double d = Math.sqrt(dx*dx + dy*dy);
		return d;
	}

	public double calcForceExertedBy(Planet p) {
		double G = 6.67E-11;
		double d = this.calcDistance(p);
		double F = G*this.mass*p.mass/(d*d);
		return F;
	}

	public double calcForceExertedByX(Planet p) {
		double dx = p.xxPos - this.xxPos;
		double F = this.calcForceExertedBy(p);
		double d = this.calcDistance(p);
		double Fx = F*dx/d;
		return Fx;
	}

	public double calcForceExertedByY(Planet p) {
		double dy = p.yyPos - this.yyPos;
		double F = this.calcForceExertedBy(p);
		double d = this.calcDistance(p);
		double Fy = F*dy/d;
		return Fy;
	}

	public double calcNetForceExertedByX(Planet[] planets) {
		double sumFx = 0.0;
		for (Planet p : planets) {
			if (this.equals(p)) {
				continue;
			}
			sumFx = sumFx + this.calcForceExertedByX(p);
		}
		return sumFx;
	}

	public double calcNetForceExertedByY(Planet[] planets) {
		double sumFy = 0.0;
		for (Planet p : planets) {
			if (this.equals(p)) {
				continue;
			}
			sumFy = sumFy + this.calcForceExertedByY(p);
		}
		return sumFy;
	}

	public void update(double dt,double Fx,double Fy) {
		double ax = Fx/this.mass;
		double ay = Fy/this.mass;
		this.xxVel = this.xxVel + dt*ax;
		this.yyVel = this.yyVel + dt*ay;
		this.xxPos = this.xxPos + dt*this.xxVel;
		this.yyPos = this.yyPos + dt*this.yyVel;
	}

	public void draw() {
		StdDraw.picture(this.xxPos,this.yyPos,"images/"+this.imgFileName);
	}
}