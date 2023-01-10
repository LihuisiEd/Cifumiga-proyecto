from django.urls import path
from rest_framework.urlpatterns import format_suffix_patterns

from . import views

urlpatterns = [
    path('', views.Index.as_view()),
    path('register/', views.RegisterView.as_view()),
    path('login/', views.LoginView.as_view()),
    path('logout/', views.LogoutView.as_view()),
    path('clientes/', views.ClienteAPIGeneral.as_view()),
    path('clientes/<int:empleado_id>', views.ClienteAPIDetalle.as_view()),
    path('certificados/', views.CertificadoAPI.as_view()),
    path('certificados/<int:clienteid>', views.CertificadoAPIDetalle.as_view()),
    path('facturas/', views.FacturaAPI.as_view()),
    path('services/', views.ServicioAPI.as_view()),
    path('services/<int:servicioid>', views.ServicioAPIDetalleID.as_view()),
    path('services/cliente/<int:clienteid>', views.ServicioAPIDetalle.as_view()),
    path('typeservice/', views.TipoServicioAPI.as_view()),
    path('tramites/', views.TramiteAPI.as_view()),
    path('tramites/<int:tramiteid>', views.TramiteAPIDetalle.as_view()),
    path('kilometrajes/', views.KilometrajeAPI.as_view()),
    path('kilometrajes/empleado/<int:empleadoid>', views.KilometrajeAPIDetalle.as_view()),
    path('kilometrajes/<int:kilometrajeid>', views.KilometrajeAPIDetalleID.as_view()),
    path('operaciones/', views.OperacionAPI.as_view()),
    path('operaciones/cliente/<int:clienteid>', views.OperacionAPIDetalle.as_view()),
    path('operaciones/<int:operacionid>', views.OperacionAPIDetalleID.as_view())
]

urlpatterns = format_suffix_patterns(urlpatterns)