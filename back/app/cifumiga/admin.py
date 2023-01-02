from django.contrib import admin
from django.contrib import admin
from django.contrib.auth.admin import UserAdmin as BaseUserAdmin
from django.contrib.auth.models import User

# Register your models here.
from . models import *

admin.site.register(Certificado)
admin.site.register(Cliente)
admin.site.register(Empleado)
admin.site.register(Servicio)
admin.site.register(Tipo_servicio)
admin.site.register(Factura)
admin.site.register(Informe)
admin.site.register(Seguridad_Cliente)
admin.site.register(Tramite)
