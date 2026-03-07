# CORRECCIÓN ENTREGA HITO 1

Se identificó que el problema general era el diagrama UML. Este no estaba bien estructurado ni seguía lo que teníamos planeado en un inicio, tenía clases redundantes que no necesitaban herencia y no tenía bien hechas las relaciones. Además, presentaba inconsistencias con la documentación, ya que se modeló como clase lo que se tenía pensado como un `ENUM`.

## Correcciones

- Eliminar la clase redundante "Responsable"; esta tenía los mismos atributos que "User", por lo que no era necesaria en el diagrama de entidades.
- Flechas no acordes con el modelado UML; ahora se puede diferenciar entre asociación, agregación y composición.
- Se identificó la clase "Asignación" como la relación entre "Solicitud" y "User". Al profesor estar enseñándonos a crear una aplicación empresarial, modelar relaciones como entidades hace más robusto el sistema en cuanto a auditorías.
