//IRequest Interface implemented by the Elevator
public interface IRequest {

	public boolean isAvailable();
	
	public void requestElevator(int callerPosition, int callerDestination, Direction callDirection);
	
	public int floorsToGo(int callerPosition);
}
