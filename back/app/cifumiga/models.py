from django.db import models

# Create your models her

class Cliente(models.Model):
   id_cliente = models.CharField(max_length=200, null=True, blank=True)
   nombre = models.TextField(null=True, blank=True)
   ruc = models.CharField(max_length=200, null=True, blank=True)
   id_certificado = models.CharField(max_length=200, null=True, blank=True)
   fecha_creacion = models.DateTimeField(auto_now=True)

   def __str__(self):
      return self.id_cliente

class Empleado(models.Model):
   usu_id = models.CharField(max_length=200, null=True, blank=True)
   nombre_emp = models.TextField(null=True, blank=True)
   email_emp = models.TextField(null=True, blank=True)
   pass_emp = models.TextField(null=True, blank=True)
   num_emp = models.TextField(null=True, blank=True)
   id_certificado = models.CharField(max_length=200, null=True, blank=True)
   emp_feccreacion = models.DateTimeField(auto_now=True)
   emp_modificacion = models.DateTimeField(auto_now=True)

class Operacion(models.Model):
   id_operacion = models.CharField(max_length=200, null=True, blank=True)
   contacto = models.TextField(null=True, blank=True)
   correo = models.CharField(max_length=200, null=True, blank=True)
   id_cliente = models.CharField(max_length=200, null=True, blank=True)

class Factura(models.Model):
   id_operacion = models.CharField(max_length=200, null=True, blank=True)
   contacto = models.TextField(null=True, blank=True)
   correo = models.CharField(max_length=200, null=True, blank=True)
   servicio_costo = models.CharField(max_length=200, null=True, blank=True)
   id_cliente = models.CharField(max_length=200, null=True, blank=True)
   doc_factura = models.TextField(null=True, blank=True)

class documentoxcliente(models.Model):
    dxa_id = models.CharField(max_length=200, null=True, blank=True)
    descripcion = models.TextField()
    id_cliente = models.CharField(max_length=200, null=True, blank=True)

class documento_doc(models.Model):
    doc_id = models.CharField(max_length=200, null=True, blank=True)
    doc_tipo = models.TextField(null=True, blank=True)
    doc_fecdoc= models.TextField(null=True,blank=True)
    doc_archivo= models.TextField(null=True,blank=True)

class informe(models.Model):
   id_informe = models.CharField(max_length=200, null=True, blank=True)
   fecha_entrega= models.DateTimeField(auto_now=True)
   id_cliente= models.CharField(max_length=200, null=True, blank=True)
   res_prom = models.TextField()
   id_tipo_fum= models.CharField(max_length=200, null=True, blank=True)
   problema_desc = models.TextField(null=True, blank=True)
   area_problema = models.CharField(max_length=200, null=True, blank=True)
   id_respuesta = models.CharField(max_length=200, null=True, blank=True)

class tipo_fumigacion(models.Model):
    id_tipo_fumigacion = models.CharField(max_length=200, null=True, blank=True)
