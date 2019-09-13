package VisualEngine.entities;

import VisualEngine.models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Entity {

	private TexturedModel model;
	private Vector3f position;
	private float rotX, rotY, rotZ;
	private float scale;

	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
			float scale) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}

	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}
	
	public void move() {
		float sensitivity = 0.8f;
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			this.rotX += sensitivity;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			this.rotX -= sensitivity;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			this.rotY += sensitivity;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			this.rotY -= sensitivity;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_Q)){
			this.rotZ += sensitivity;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_Z)){
			this.rotZ -= sensitivity;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_P)){
			this.position.x=0;
			this.position.y=0;
			this.position.z=-50;
			this.rotX=0;
			this.rotY=0;
			this.rotZ=0;
		}
		float mouseSensitivity = 0.1f;
		float mouseWheelSensitivity =0.001f;
		if(Mouse.isButtonDown(0)){
			int value = Mouse.getDX();
			this.position.x+=value*mouseSensitivity;
		}
		if(Mouse.isButtonDown(0)){
			int value = Mouse.getDY();
			this.position.y+=value*mouseSensitivity;
		}
		if(Mouse.isButtonDown(1)){
			int value = Mouse.getDX();
			this.rotY+=value*mouseSensitivity;
		}
		if(Mouse.isButtonDown(1)){
			int value = Mouse.getDY();
			this.rotX-=value*mouseSensitivity;
		}
		int value = Mouse.getDWheel();
		this.position.z+=value*mouseWheelSensitivity;
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

}
