package simulaltion.queue;

/**
 * Outbound Control Message Queue를 관리하는 클래스
 * 
 * @author		박병권
 * @since       2014-01-29
 * @version     0.1       
 */
public class MsgQueueImpl extends MsgQueue
{
	private static MsgQueueImpl outboundMsgQueue; //Outbound Message Queue
	
	/**
	 * Class constructor
	 * Message Queue Instance를 여기서 생성함
	 */
	static
	{
		outboundMsgQueue = new MsgQueueImpl();
	}
	
	/*
	 * Outbound Message Queue Instance를 획득할 수 있는 클래스 메서드
	 */
	public static MsgQueueImpl getInstance()
	{
		return outboundMsgQueue;
	}
	
	public MsgQueueImpl()
	{
		super();
	}
	
	/* 박창현 추가
	 * (non-Javadoc)
	 * @see msg.queue.MsgQueue#append(msg.node.MsgNode)
	 */
	public synchronized boolean append(QueueNode msgNode)
	{		
		if (super.append(msgNode))
		{
			notifyAll();
		}
		
		return true;
	}
	/* 박창현 추가
	 * (non-Javadoc)
	 * @see msg.queue.MsgQueue#append(msg.node.MsgNode)
	 */
	public synchronized QueueNode poll() 
	{
		QueueNode node = null;
		
		while((node = super.poll()) == null)
		{
			try 
			{
				wait();
			} 
			catch (InterruptedException e) 
			{
				//e.printStackTrace();
			}
		}
		
		return node;
	}

}
