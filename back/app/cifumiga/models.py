from email.policy import default
from enum import unique
from unittest.util import _MAX_LENGTH
from django.contrib.auth.models import User
from django.db import models
from django.contrib.auth.models import AbstractUser

# Create your models her
class Cliente(models.Model):
    cliente_nombre = models.CharField(max_length=255)
    cliente_ruc = models.CharField(max_length=200, null=True, blank=True)
    cliente_contacto = models.CharField(max_length=100, null=True, blank=True)
    cliente_telefono = models.CharField(max_length=50, null=True, blank=True)
    cliente_correo = models.CharField(max_length=50, null=True, blank=True)
    cli_fecregistro = models.DateTimeField(auto_now=True)
    cli_fecmodificacion = models.DateTimeField(auto_now=True)

    def __str__(self):
        return self.cliente_nombre

class Empleado(AbstractUser):
    name = models.CharField(max_length=255)
    email = models.CharField(max_length=255, unique=True)
    password = models.CharField(max_length=255)
    celular = models.CharField(max_length=9)
    emp_feccreacion = models.DateTimeField(auto_now_add=True)
    emp_modificacion = models.DateTimeField(auto_now=True)

    username = None
    is_staff = True

    USERNAME_FIELD = 'email'
    REQUIRED_FIELDS = []

class Certificado(models.Model):
    cliente = models.ForeignKey(Cliente, related_name='certificado_cliente', on_delete=models.CASCADE)
    fecha_inicio = models.DateField()
    fecha_fin = models.DateField()
    doc_certificado = models.FileField(upload_to="certificados/", null=True, blank=True)
    cert_feccreacion = models.DateTimeField(auto_now=True)


class Operacion(models.Model):
    cliente = models.ForeignKey(Cliente, related_name='operacion_cliente', on_delete=models.CASCADE)
    operacion_contacto = models.CharField(max_length=50)
    operacion_correo = models.CharField(max_length=200)



class Factura(models.Model):
    cliente = models.ForeignKey(Cliente, related_name='factura_cliente', on_delete=models.CASCADE)
    factura_contacto = models.CharField(max_length=50)
    factura_correo = models.CharField(max_length=200, null=True, blank=True)
    factura_costo = models.CharField(max_length=20)
    doc_factura = models.FileField(upload_to="facturas/", null=True, blank=True)

    def __str__(self):
        return self.cliente.cliente_nombre

class Tipo_servicio(models.Model):
    servicio_nombre = models.CharField(max_length=100)
    def __str__(self):
        return self.servicio_nombre

class Servicio(models.Model):
    tipo = models.ForeignKey(Tipo_servicio, related_name='tipo_servicio', on_delete=models.CASCADE)
    cliente = models.ForeignKey(Cliente, related_name='cliente', on_delete=models.CASCADE)
    servicio_desc = models.TextField(max_length=150)
    servicio_area = models.CharField(max_length=50)
    servicio_dim = models.CharField(max_length=20)
    servicio_frec = models.CharField(max_length=100)
    servicio_precio = models.CharField(max_length=20)
    doc_contrato = models.FileField(upload_to="contratos/", null=True, blank=True)
    doc_proforma = models.FileField(upload_to="proformas/", null=True, blank=True)
    doc_ficha = models.FileField(upload_to="fichas/", null=True, blank=True)

    def __str__(self):
        return self.tipo.servicio_nombre


class Tipo_Documento(models.Model):
    documento_nombre = models.CharField(max_length=100)


class Documento(models.Model):
    documento_tipo = models.ForeignKey(Tipo_Documento, related_name='tipo_documento', on_delete=models.CASCADE)
    doc_archivo = models.FileField(upload_to="archivos/", null=True, blank=True)
    documento_fecha = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return self.documento_tipo.tipo_documento

class Documento_Cliente(models.Model):
    cliente = models.ForeignKey(Cliente, related_name='documento_cliente', on_delete=models.CASCADE)
    documento = models.ForeignKey(Documento, related_name='documento', on_delete=models.CASCADE)


class Informe(models.Model):
    informe_mes = models.CharField(max_length=20)
    informe_sostenimiento = models.TextField(max_length=150)
    doc_informe = models.FileField(upload_to="informes/", null=True, blank=True)
    
    def __str__(self):
        return self.info_mes


class Seguridad_Cliente(models.Model):
    cliente = models.ForeignKey(Cliente, related_name='seguridad_cliente', on_delete=models.CASCADE)
    doc_iperc = models.FileField(upload_to="ipercs/", null=True, blank=True)
    doc_ats = models.FileField(upload_to="atss/", null=True, blank=True)
    
    def __str__(self):
        return self.cliente.cliente_nombre

class Seguridad(models.Model):
    doc_productos = models.FileField(upload_to="productos/", null=True, blank=True)
    doc_pets = models.FileField(upload_to="pets/", null=True, blank=True)

class Tramite(models.Model):
    tipo = models.ForeignKey(Tipo_servicio, related_name='tipo_tramite', on_delete=models.CASCADE)
    cliente = models.ForeignKey(Cliente, related_name='tramite_cliente', on_delete=models.CASCADE)
    tramite_fecha = models.DateField()
    direccion = models.CharField(max_length=200)
    referencia = models.CharField(max_length=200)
    tramite_contacto = models.CharField(max_length=100)
    tramite_telefono = models.CharField(max_length=20)
    tramite_nivel_1 = models.BooleanField(default=False)
    tramite_nivel_2 = models.BooleanField(default=False)
    tramite_nivel_3 = models.BooleanField(default=False)
    tramite_nivel_4 =  models.BooleanField(default=False)
    problemas = models.CharField(max_length=300)
    condicion_subestandar = models.CharField(max_length=300)

    def __str__(self):
        return self.cliente.cliente_nombre
    
class Comentario(models.Model):
    cliente = models.ForeignKey(Cliente, related_name='comentario_cliente', on_delete=models.CASCADE)
    comentario = models.CharField(max_length=200)

    def __str__(self):
        return self.cliente.cliente_nombre

class Kilometraje(models.Model):
    empleado = models.ForeignKey(Empleado, related_name='empleado', on_delete=models.CASCADE)
    fecha = models.DateField()
    placa = models.CharField(max_length=7, unique=True)
    kilometraje_inicio = models.DecimalField(max_digits=8, decimal_places=2)
    kilometraje_fin = models.DecimalField(max_digits=8,decimal_places=2)
    kilometraje_total = models.DecimalField(max_digits=8,decimal_places=2, null=True, blank=True)

    def __str__(self):
        return self.empleado.name