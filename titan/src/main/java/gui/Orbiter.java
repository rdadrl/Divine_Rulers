package gui;

import utils.Vector3D;

public interface Orbiter {
	Vector3D getVelocity();
	Vector3D getPos();
	double getMass();
	void setPos(Vector3D pos);
	void setVel(Vector3D vel);
}
