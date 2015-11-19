package doodle.tearne.typeclass

import scala.language.higherKinds

trait Functor[T[_]] {
  def map[A,B](from: T[A])(f: A => B): T[B]
}
//object Functor {
//  implicit object listInstance extends Functor[List]{
//    def map[A,B](from: List[A])(f: A => B) = from.map(f)
//  }
//}

trait Applicative[T[_]] extends Functor[T] {
  def zip[A,B](ta: T[A], tb: T[B]): T[(A,B)]
  def point[A](a: A): T[A]
}
//object Applicative {
//  implicit object listInstance extends Applicative[List]{
//    def zip[A, B](a: List[A], b: List[B]) = a.zip(b)
//    def point[A](a: A) = List(a)
//    def map[A,B](from: List[A])(f: A => B) = from.map(f) //TODO fix duplication?
//  }
//}

trait Monad[T[_]] extends Functor[T] {
  def flatMap[A,B](ta: T[A])(f: A => T[B]): T[B]
  def point[A](a: A): T[A]
}
//object Monad {
//  implicit object listInstance extends Monad[List]{
//    def flatMap[A, B](a: List[A])(f: A => List[B]) = a.flatMap(f)
//    def point[A](a: A) = List(a)
//    def map[A,B](from: List[A])(f: A => B) = from.map(f) //TODO fix duplication?
//  }
//}

trait Scanable[T[_]] {
  def scanLeft[A, B](instance: T[A])(seed: B)(f: (B, A) => B): T[B]
}
//object Scanable {
//  implicit object listInstance extends Scanable[List] {
//    def scanLeft[A, B](instance: List[A])(seed: B)(f: (B, A) => B) =
//      instance.scanLeft(seed)(f)
//  }
//}