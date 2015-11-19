package doodle

//TODO why would I extend EventStreamImage syntax in both places?
package object syntax extends /*EventStreamImageSyntax with*/ AngleSyntax with NormalizedSyntax with UnsignedByteSyntax {
  object angle extends AngleSyntax
  object normalized extends NormalizedSyntax
  object uByte extends UnsignedByteSyntax
  object eventStreamImageSyntax extends EventStreamImageSyntax
}
