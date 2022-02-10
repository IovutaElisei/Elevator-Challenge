//IResponse interface implemented by the DC-Tower
public interface IResponse {

	public void pickedUp(int elevatorId, int floor);
	
	public void dropedOff(int elevatorId, int floor);
}
