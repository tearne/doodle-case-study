package doodle.tearne.typeclass

object NormalInstances {
  type Id[A] = A
  
  implicit object normalInstances
      extends Functor[Id] 
      with Monad[Id] 
      with Applicative[Id] 
      with Scanable[Id] {
    def map[A,B](a: Id[A])(f: A => B) = f(a)
    def zip[A,B](a: Id[A], b: Id[B]) = (a,b)
    def point[A](a: A) = a
    def flatMap[A,B](a: Id[A])(f: A => Id[B]) = f(a)
    def scanLeft[A, B](instance: Id[A])(seed: B)(f: (B, A) => B) = f(seed, instance)
  }
}