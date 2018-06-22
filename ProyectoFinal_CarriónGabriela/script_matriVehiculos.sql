/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     18/7/2017 21:49:15                           */
/*==============================================================*/


drop table ASIGNACION;

drop table CLIENTE;

drop table ESTADO_REVISION;

drop table PERSONAL;

drop table REQUISITOS;

drop table REVISION;

drop table ROL;

drop table TURNO;

drop table VEHICULOS;

/*==============================================================*/
/* Table: ASIGNACION                                            */
/*==============================================================*/
create table ASIGNACION (
   ID_ASIGNACION        INT8                 not null,
   CEDULA_CLI           VARCHAR(10)          null,
   CEDULA_PER           VARCHAR(10)          null,
   ID_TURNO             INT4                 null,
   PLACA                VARCHAR(7)           null,
   COD_ESTADOREVISION   INT8                 null,
   constraint PK_ASIGNACION primary key (ID_ASIGNACION)
);

/*==============================================================*/
/* Table: CLIENTE                                               */
/*==============================================================*/
create table CLIENTE (
   CEDULA_CLI           VARCHAR(10)          not null,
   NOMBRES_CLI          VARCHAR(40)          null,
   APELLIDOS_CLI        VARCHAR(50)          null,
   PLACA                VARCHAR(7)           null,
   constraint PK_CLIENTE primary key (CEDULA_CLI)
);

/*==============================================================*/
/* Table: ESTADO_REVISION                                       */
/*==============================================================*/
create table ESTADO_REVISION (
   COD_ESTADOREVISION   INT8                 not null,
   ESTADO               VARCHAR(25)          null,
   constraint PK_ESTADO_REVISION primary key (COD_ESTADOREVISION)
);

/*==============================================================*/
/* Table: PERSONAL                                              */
/*==============================================================*/
create table PERSONAL (
   CEDULA_PER           VARCHAR(10)          not null,
   NOMBRES_PER          VARCHAR(40)          null,
   APELLIDOS_PER        VARCHAR(50)          null,
   DIRECCION_PER        VARCHAR(50)          null,
   TELEFONO_PER         VARCHAR(15)          null,
   CORREO_PER           VARCHAR(40)          null,
   ID_ROL               VARCHAR(5)           null,
   CONTRASENIA          VARCHAR(12)          null,
   constraint PK_PERSONAL primary key (CEDULA_PER)
);

/*==============================================================*/
/* Table: REQUISITOS                                            */
/*==============================================================*/
create table REQUISITOS (
   ID_REQUISITO         VARCHAR(5)           not null,
   REQUISITO            VARCHAR(30)          null,
   constraint PK_REQUISITOS primary key (ID_REQUISITO)
);

/*==============================================================*/
/* Table: REVISION                                              */
/*==============================================================*/
create table REVISION (
   ID_ASIGNACION        INT8                 not null,
   ID_REQUISITO         VARCHAR(5)           not null,
   constraint PK_REVISION primary key (ID_ASIGNACION, ID_REQUISITO)
);

/*==============================================================*/
/* Table: ROL                                                   */
/*==============================================================*/
create table ROL (
   ID_ROL               VARCHAR(5)           not null,
   ROL                  VARCHAR(20)          null,
   constraint PK_ROL primary key (ID_ROL)
);

/*==============================================================*/
/* Table: TURNO                                                 */
/*==============================================================*/
create table TURNO (
   ID_TURNO             INT4                 not null,
   FECHA_TURNO          TIMESTAMP            null,
   TIPO_TRAMITE         VARCHAR(40)          null,
   constraint PK_TURNO primary key (ID_TURNO)
);

/*==============================================================*/
/* Table: VEHICULOS                                             */
/*==============================================================*/
create table VEHICULOS (
   PLACA                VARCHAR(7)           not null,
   ANIO                 INT4                 null,
   COLOR                VARCHAR(20)          null,
   MARCA                VARCHAR(25)          null,
   NRO_MOTOR            VARCHAR(25)          null,
   NRO_CHASIS           VARCHAR(25)          null,
   constraint PK_VEHICULOS primary key (PLACA)
);

alter table ASIGNACION
   add constraint FK_ASIGNACI_REFERENCE_VEHICULO foreign key (PLACA)
      references VEHICULOS (PLACA)
      on delete restrict on update restrict;

alter table ASIGNACION
   add constraint FK_ASIGNACI_REFERENCE_ESTADO_R foreign key (COD_ESTADOREVISION)
      references ESTADO_REVISION (COD_ESTADOREVISION)
      on delete restrict on update restrict;

alter table ASIGNACION
   add constraint FK_ASIGNACI_REFERENCE_CLIENTE foreign key (CEDULA_CLI)
      references CLIENTE (CEDULA_CLI)
      on delete restrict on update restrict;

alter table ASIGNACION
   add constraint FK_ASIGNACI_REFERENCE_PERSONAL foreign key (CEDULA_PER)
      references PERSONAL (CEDULA_PER)
      on delete restrict on update restrict;

alter table ASIGNACION
   add constraint FK_ASIGNACI_REFERENCE_TURNO foreign key (ID_TURNO)
      references TURNO (ID_TURNO)
      on delete restrict on update restrict;

alter table PERSONAL
   add constraint FK_PERSONAL_REFERENCE_ROL foreign key (ID_ROL)
      references ROL (ID_ROL)
      on delete restrict on update restrict;

alter table REVISION
   add constraint FK_REVISION_REFERENCE_ASIGNACI foreign key (ID_ASIGNACION)
      references ASIGNACION (ID_ASIGNACION)
      on delete restrict on update restrict;

alter table REVISION
   add constraint FK_REVISION_REFERENCE_REQUISIT foreign key (ID_REQUISITO)
      references REQUISITOS (ID_REQUISITO)
      on delete restrict on update restrict;

