from rest_framework import serializers
from cifumiga.models import *



class ClienteSerializer(serializers.ModelSerializer):
    class Meta:
        model = Cliente
        fields = '__all__'
        read_only_fields = ['cli_fecregistro', 'cli_fecmodificacion']

class EmpleadoSerializer(serializers.ModelSerializer):
    class Meta:
        model = Empleado
        fields = ['id', 'name', 'email', 'celular', 'password', 'emp_feccreacion', 'emp_modificacion']
        extra_key = {
            "password": {'write_only':True}
        }
    
    def create(self, validated_data):
        password = validated_data.pop('password', None)
        instance = self.Meta.model(**validated_data)
        if password is not None:
            instance.set_password(password)
        instance.save()
        return instance   

class CertificadoSerializer(serializers.ModelSerializer):
    class Meta:
        model = Certificado
        fields = '__all__'
    
    def to_representation(self, instance):
        representation = super().to_representation(instance)
        representation['cliente'] = instance.cliente.cli_nombre
        return representation

class FacturaSerializer(serializers.ModelSerializer):
    class Meta:
        model = Factura
        fields = '__all__'
    
    def to_representation(self, instance):
        representation = super().to_representation(instance)
        representation['cliente'] = instance.cliente.cli_nombre
        return representation


class ServicioSerializer(serializers.ModelSerializer):
    class Meta:
        model = Servicio
        fields = '__all__'
    def to_representation(self, instance):
        representation = super().to_representation(instance)
        representation['cliente'] = instance.cliente.cliente_nombre
        representation['tipo'] = instance.tipo.servicio_nombre
        return representation

class TipoServicioSerializer(serializers.ModelSerializer):
    class Meta:
        model = Tipo_servicio
        fields = '__all__'


class TramiteSerializer(serializers.ModelSerializer):
    class Meta:
        model = Tramite
        fields = '__all__'
    def to_representation(self, instance):
        representation = super().to_representation(instance)
        representation['cliente'] = instance.cliente.cliente_nombre
        representation['tipo'] = instance.tipo.servicio_nombre
        return representation