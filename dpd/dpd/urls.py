"""dpd URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.11/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'^$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'^$', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.conf.urls import url, include
    2. Add a URL to urlpatterns:  url(r'^blog/', include('blog.urls'))
"""
from django.conf.urls import url
from django.contrib import admin
from dpd_app.views import *

urlpatterns = [
    url(r'^admin/', admin.site.urls),

    ##########login start
    # url(r'^$',display_login),
    # url(r'^display_login', display_login, name="display_login"),
    # url(r'^check_login', check_login, name="check_login"),
    # url(r'^logout',logout,name="logout"),
    ##########login end

    ################Admin start
    # url(r'^show_home_admin',show_home_admin,name="show_home_admin"),
    # url(r'^view_users_admin',view_users_admin,name="view_users_admin"),
    url(r'^delete',delete,name="delete"),
    # url(r'^view_drug_post_admin',view_drug_post_admin,name="view_drug_post_admin"),
    url(r'^take_action',take_action,name="take_action"),
    ################Admin end

    ################
    url(r'^register',register,name="register"),
    url(r'^find_login',find_login,name="find_login"),
    url(r'^upload_post',upload_post,name="upload_post"),
    url(r'^get_all_posts',get_all_posts,name="get_all_posts"),
    url(r'^get_my_posts',get_my_posts,name="get_my_posts"),
    url(r'^view_drug_posts',view_drug_posts,name="view_drug_posts"),
    url(r'^view_users',view_users,name="view_users"),
    url(r'^admin_view_drug_posts',admin_view_drug_posts,name="admin_view_drug_posts"),
    url(r'^get_my_reports',get_my_reports,name="get_my_reports"),
    url(r'^post_delete',post_delete,name="post_delete"),
    url(r'^get_user_details',get_user_details,name="get_user_details"),
    url(r'^update_user_details',update_user_details,name="update_user_details"),
    ################

]
