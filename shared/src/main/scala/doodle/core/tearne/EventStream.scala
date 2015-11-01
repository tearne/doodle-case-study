package doodle.core.tearne

sealed trait EventStream[Input, Output] {
  val downstreamClients = collection.mutable.Set.empty[EventStream[Output, _]]

  def map[B](f: Output => B): EventStream[Output, B] = {
    val downstream = Map(this, f)
    downstreamClients add downstream
    downstream
  }
  def join[B](that: EventStream[_,B]): EventStream[Nothing,(A,B)] = {
    val downstream = Join(this, that)
    
  }
  //def scan[B](seed: B)(f: (A, B) => B): ES[B] = Scan(this, seed, f)

  def push(value: Input)
}
object EventStream {
}

final case class Map[In, Out](source: EventStream[_, In], f: In => Out) extends EventStream[In, Out] {
  def push(value: In) = {
    val result = f(value)
    downstreamClients.foreach(_ push result)
  }

}
final case class Join[A, B](
    left: EventStream[_, A],
    right: EventStream[_, B]) extends EventStream[Nothing, (A, B)] {
  def push
}

//final case class Scan[A,B](source: EventStream[A], seed: B, f: (A, B) => B) extends EventStream[B]
final case class Source[A](callbackHandler: ((A => Unit)) => Unit) extends EventStream[Unit, A]