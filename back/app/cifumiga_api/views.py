from django.http import Http404
from django.shortcuts import render

from cifumiga.models import *

#Imports relacionado a rest framework
from rest_framework.views import APIView
from rest_framework.response import Response
from cifumiga_api.serializers import *
from rest_framework import status
from rest_framework.exceptions import AuthenticationFailed

import jwt, datetime

import cloudinary.uploader

import requests

# Create your views here.
class Index(APIView):
    def get(self, request):
        context = {
            'status':True,
            'content':'api activa'
        }
        return Response(context)

class ClienteAPIGeneral(APIView):
    def get(self, request):
        cliLista = Cliente.objects.all()
        serializer = ClienteSerializer(cliLista, many=True)

        return Response(serializer.data)

    def post(self, request):
        serializer = ClienteSerializer(data=request.data)
        
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class ClienteAPIDetalle(APIView):
    def get_object(self, empleado_id):
        try:
            return Cliente.objects.get(pk=empleado_id)
        except Cliente.DoesNotExist:
            raise Http404

    def get(self, request, empleado_id):
        empListaDet = self.get_object(empleado_id)
        empSerializer = ClienteSerializer(empListaDet)
        
        return Response(empSerializer.data)

    def put(self, request, empleado_id):
        empListaDet = self.get_object(empleado_id)
        empSerializer = ClienteSerializer(empListaDet, data=request.data)

        if empSerializer.is_valid():
            empSerializer.save()
            return Response(empSerializer.data)
        
        return Response(empSerializer.errors, status=status.HTTP_400_BAD_REQUEST)
    
    def delete(self, request, empleado_id):
        empListaDet = self.get_object(empleado_id)
        empListaDet.delete()

        return Response(status=status.HTTP_204_NO_CONTENT)
class RegisterView(APIView):
    def post(self, request):
        serializers = EmpleadoSerializer(data=request.data)
        serializers.is_valid(raise_exception=True)
        serializers.save()
        return response(serializers.data)

class LoginView(APIView):
    def post (self, request):
        email = request.data['email']
        password = request.data['password']

        emp = Empleado.objects.filter(email=email).first()

        if emp is None:
            raise AuthenticationFailed('Empleado no encontrado!')

        if not emp.check_password(password):
            raise AuthenticationFailed('Contraseña incorrecta!')

        payload = {
            'id': emp.id,
            'name':emp.name,
            'email':emp.email,
            'celular':emp.celular,
            'exp': datetime.datetime.utcnow() + datetime.timedelta(minutes=60),
            'iat': datetime.datetime.utcnow()
        }

        token = jwt.encode(payload, 'secret', algorithm='HS256').decode('utf-8')

        response = Response()

        response.set_cookie(key='jwt', value=token, httponly=True)
        response.data = {
            'jwt': token
        }
        return response


class EmpleadoAPIGeneral(APIView):


    def get(self, request):
        token = request.COOKIES.get('jwt')

        if not token:
            raise AuthenticationFailed('Usuario no autenticado')
        try:
            payload = jwt.decode(token, 'secret', algorithm=['HS256'])
        except jwt.ExpiredSignatureError:
            raise AuthenticationFailed('Usuario no autenticado')

        emp = Empleado.objects.filter(id=payload['id']).first()
        serializer =  EmpleadoSerializer(emp)

        return Response(serializer.data)

    def put(self, request):

        token = request.COOKIES.get('jwt')

        if not token:
            raise AuthenticationFailed('Usuario no autenticado')
        try:
            payload = jwt.decode(token, 'secret', algorithms=['HS256'])
        except jwt.ExpiredSignatureError:
            raise AuthenticationFailed('Usuario no autenticado')
        emp = Empleado.objects.filter(id=payload['id']).first()

        serializer = EmpleadoSerializer(emp, data=request.data)

        context = {
            'status':True,
            'content':serializer.data   
        }

        return Response(context) 

    def delete(self, request):
        token = request.COOKIES.get('jwt')

        if not token:
            raise AuthenticationFailed('Usuario no autenticado')
        try:
            payload = jwt.decode(token, 'secret', algorithm=['HS256'])
        except jwt.ExpiredSignatureError:
            raise AuthenticationFailed('Usuario no autenticado')
        emp = Empleado.objects.filter(id=payload['id']).first()

        emp.delete()

        context = {
            'status': True,
            'message':'Usuario Eliminado'
        }

        return Response(context)

class LogoutView(APIView):
    def post(self, request):
        response = Response()
        response.delete_cookie('jwt')
        response.data = {
            "message":"Logout successful"
        }
        return response

class CertificadoAPI(APIView):
    def get(self, request):
        certificado = Certificado.objects.all()
        serializer = CertificadoSerializer(certificado, many=True)

        return Response(serializer.data)
    
    def post(self, request):
        serializer = CertificadoSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class CertificadoAPIDetalle(APIView):
    def get_object(self, clienteid):
        try:
            return Certificado.objects.get(pk=clienteid)
        except Certificado.DoesNotExist:
            raise Http404

    def get(self, request, clienteid):
        certificado = Certificado.objects.filter(cliente=clienteid)
        serializer = CertificadoSerializer(certificado, many=True)

        return Response(serializer.data)
    
    def put(self, request, clienteid):
        certificado = self.get_object(clienteid)
        serializer = CertificadoSerializer(certificado, data=request.data)

        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
    

    def delete(self, request, clienteid, format=None):
        certificado = Certificado.objects.get(pk=clienteid)
        certificado.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)


class FacturaAPI(APIView):
    def get(self, request):
        factura = Factura.objects.all()
        serializer = FacturaSerializer(factura, many=True)

        return Response(serializer.data)
    
    def post(self, request):
        serializer = FacturaSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


class ServicioAPI(APIView):
    def get(self, request):
        servicio = Servicio.objects.all()
        serializer = ServicioSerializer(servicio, many=True)

        return Response(serializer.data)
    
    def post(self, request):
        serializer = ServicioSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


class ServicioAPIDetalle(APIView):
    def get_object(self, clienteid):
        try:
            return Servicio.objects.get(pk=clienteid)
        except Servicio.DoesNotExist:
            raise Http404

    def get(self, request, clienteid):
        servicio = Servicio.objects.filter(cliente=clienteid)
        serializer = ServicioSerializer(servicio, many=True)

        return Response(serializer.data)
    

    def delete(self, request, clienteid, format=None):
        servicio = Servicio.objects.get(pk=clienteid)
        servicio.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)


class TipoServicioAPI(APIView):
    def get(self, request):
        tiposervicio = Tipo_servicio.objects.all()
        serializer = TipoServicioSerializer(tiposervicio, many=True)

        return Response(serializer.data)

class TramiteAPI(APIView):
    def get(self, request):
        tramite = Tramite.objects.all()
        serializer = TramiteSerializer(tramite, many=True)

        return Response(serializer.data)

    def post(self, request):
        serializer = TramiteSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)