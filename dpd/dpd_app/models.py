from django.db import models

# Create your models here.
class Userss(models.Model):
    u_id = models.IntegerField(primary_key=True)
    name=models.CharField(max_length=40)
    username = models.CharField(max_length=40)
    phone = models.CharField(max_length=20)
    email_id=models.CharField(max_length=20)
    password = models.CharField(max_length=30)
    user_image=models.CharField(max_length=255)
    encoded_image=models.TextField(max_length=8000)

class Posts(models.Model):
    p_id = models.IntegerField(primary_key=True)
    p_text=models.CharField(max_length=255)
    p_image=models.CharField(max_length=255)
    time=models.CharField(max_length=40)
    date=models.CharField(max_length=40)
    name=models.CharField(max_length=40)
    username = models.CharField(max_length=40)
    phone = models.CharField(max_length=20)
    email_id=models.CharField(max_length=20)
    encoded_image=models.CharField(max_length=8000)

class Drug_Posts(models.Model):
    p_id = models.IntegerField(primary_key=True)
    p_text=models.CharField(max_length=255)
    p_image=models.CharField(max_length=255)
    time=models.CharField(max_length=40)
    date=models.CharField(max_length=40)
    name=models.CharField(max_length=40)
    username = models.CharField(max_length=40)
    phone = models.CharField(max_length=20)
    email_id=models.CharField(max_length=20)
    status=models.CharField(max_length=40)
    encoded_image=models.CharField(max_length=8000)

class Police_DBs(models.Model):
    p_id = models.IntegerField(primary_key=True)
    p_text=models.CharField(max_length=255)
    p_image=models.CharField(max_length=255)
    time=models.CharField(max_length=40)
    date=models.CharField(max_length=40)
    name=models.CharField(max_length=40)
    username = models.CharField(max_length=40)
    phone = models.CharField(max_length=20)
    email_id=models.CharField(max_length=20)
    encoded_image=models.CharField(max_length=8000)