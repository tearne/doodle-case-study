package doodle.core.tearne

trait Reciever[A] {
  def rx(value: A)
}

/*sealed*/ trait EventStream[Output] {
  val downstream = collection.mutable.Set.empty[Reciever[Output]]

  def subscribe(subscriber: Reciever[Output]){
    downstream add subscriber
  }
  
  def map[B](f: Output => B): EventStream[B] = {
    val node = Map(this, f)
    subscribe(node)
    node
  }
  def join[B](that: EventStream[B]): EventStream[(Output,B)] = {
    val node = Join(this, that)
    this.subscribe(node.leftReciever)
    that.subscribe(node.rightReciever)
    node
  }
  //def scan[B](seed: B)(f: (A, B) => B): ES[B] = Scan(this, seed, f)

  def send(value: Output){
    downstream.foreach(_.rx(value))
  }
}

final case class Map[In, Out](source: EventStream[In], f: In => Out) 
    extends Reciever[In] with EventStream[Out]{
  def rx(value: In) = send(f(value))
}

final case class Join[L, R](left: EventStream[L], right: EventStream[R]) 
    extends EventStream[(L,R)] {
	//Don't like var!
	var lastLeft: Option[L] = None
	var lastRight: Option[R] = None
  
	val leftReciever = new Reciever[L]{  
    def rx(value: L) = {
      lastLeft = Some(value)
      go()
    }
  }
  
  val rightReciever = new Reciever[R]{
    def rx(value: R) = {
      lastRight = Some(value)
      go()
    }
  }
  
  def go() {for{
    leftValue <- lastLeft
    rightValue <- lastRight
  } send(leftValue, rightValue)}
}

//final case class Scan[A,B](source: EventStream[A], seed: B, f: (A, B) => B) extends EventStream[B]
final case class Source[A](callbackHandler: ((A => Unit)) => Unit) extends EventStream[A]