package doodle.tearne.typeclass

object ListInstances {
  implicit object listInstances 
      extends Functor[List] 
      with Monad[List] 
      with Applicative[List] 
      with Scanable[List] {
    def map[A,B](from: List[A])(f: A => B) = from.map(f)
    def zip[A, B](a: List[A], b: List[B]) = a.zip(b)
    def point[A](a: A) = List(a)
    def flatMap[A, B](a: List[A])(f: A => List[B]) = a.flatMap(f)
    def scanLeft[A, B](instance: List[A])(seed: B)(f: (B, A) => B) =
      instance.scanLeft(seed)(f)
  }
  
  /*
   * Would this have been OK too?
  
  implicit val listInstances = new Functor[List] 
      with Monad[List] 
      with Applicative[List] 
      with Scanable[List] { ... }
  
   */
}