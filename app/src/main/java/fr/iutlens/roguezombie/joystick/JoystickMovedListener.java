package fr.iutlens.roguezombie.joystick;

/**
 * Created by marvyn.duvauchelle on 03/02/15.
 */

public interface JoystickMovedListener {
    public void OnMoved(int pan, int tilt);
    public void OnReleased();
    public void OnReturnedToCenter();
}
