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
    path('services/<int:clienteid>', views.ServicioAPIDetalle.as_view()),
    path('typeservice/', views.TipoServicioAPI.as_view()),
    path('levelservice/', views.NivelServicioAPI.as_view()),
    path('levelservice/<int:tipo_id>', views.NivelServicioAPIDetalle.as_view())
]

urlpatterns = format_suffix_patterns(urlpatterns)