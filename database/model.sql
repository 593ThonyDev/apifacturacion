-- MySQL Script generated by MySQL Workbench
-- Fri Apr 26 10:47:53 2024
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema facturacionSRI
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema facturacionSRI
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `facturacionSRI` DEFAULT CHARACTER SET utf8 ;
USE `facturacionSRI` ;

-- -----------------------------------------------------
-- Table `facturacionSRI`.`plan`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `facturacionSRI`.`plan` (
  `idplan` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NULL,
  `cantidad` VARCHAR(45) NULL,
  `precio` DECIMAL(6,2) NULL,
  `periodo` VARCHAR(45) NULL,
  `descripcion` LONGTEXT NULL,
  `status` ENUM('ONLINE', 'OFFLINE') NULL,
  PRIMARY KEY (`idplan`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `facturacionSRI`.`emisor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `facturacionSRI`.`emisor` (
  `idemisor` INT NOT NULL AUTO_INCREMENT,
  `fkplan` INT NOT NULL,
  `ruc` VARCHAR(13) NULL,
  `ambiente` VARCHAR(1) NULL,
  `tipoemision` VARCHAR(1) NULL,
  `razonsocial` VARCHAR(300) NULL,
  `nombrecomercial` VARCHAR(300) NULL,
  `direccionmatriz` VARCHAR(300) NULL,
  `contribuyenteespecial` VARCHAR(13) NULL,
  `obligadocontabilidad` VARCHAR(2) NULL,
  `agenteretencion` VARCHAR(255) NULL,
  `tipocontribuyente` TINYINT(1) NULL,
  `regimenmicroempresa` TINYINT(1) NULL,
  `dirlogo` VARCHAR(255) NULL,
  `dirfirma` VARCHAR(255) NULL,
  `dirautorizados` VARCHAR(255) NULL,
  `passfirma` VARCHAR(255) NULL,
  `correoremitente` VARCHAR(255) NULL,
  `status` ENUM('ONLINE', 'OFFLINE') NULL,
  `fechainicio` DATETIME NULL,
  `fechafin` DATETIME NULL,
  `cantcontratada` INT NULL,
  `cantidadusada` INT NULL,
  `created` DATETIME NULL,
  PRIMARY KEY (`idemisor`),
  INDEX `fk_emisor_plan_idx` (`fkplan` ASC),
  CONSTRAINT `fk_emisor_plan`
    FOREIGN KEY (`fkplan`)
    REFERENCES `facturacionSRI`.`plan` (`idplan`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `facturacionSRI`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `facturacionSRI`.`user` (
  `iduser` INT NOT NULL,
  `fkemisor` INT NOT NULL,
  `username` VARCHAR(45) NULL,
  `password` VARCHAR(255) NULL,
  `email` VARCHAR(255) NULL,
  `nombres` VARCHAR(45) NULL,
  `apellidos` VARCHAR(45) NULL,
  `role` ENUM('ADMINISTRADOR', 'EMISOR', 'CAJERO') NULL,
  `status` ENUM('ONLINE', 'OFFLINE') NULL,
  PRIMARY KEY (`iduser`),
  INDEX `fk_user_emisor1_idx` (`fkemisor` ASC),
  CONSTRAINT `fk_user_emisor1`
    FOREIGN KEY (`fkemisor`)
    REFERENCES `facturacionSRI`.`emisor` (`idemisor`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `facturacionSRI`.`iva`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `facturacionSRI`.`iva` (
  `idiva` INT NOT NULL,
  `codigoporcentaje` VARCHAR(4) NULL,
  `nombre` VARCHAR(255) NULL,
  `tarifa` DECIMAL(10,2) NULL,
  `status` ENUM('ONLINE', 'OFFLINE') NULL,
  PRIMARY KEY (`idiva`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `facturacionSRI`.`ice`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `facturacionSRI`.`ice` (
  `idice` INT NOT NULL AUTO_INCREMENT,
  `codigoporcenaje` VARCHAR(4) NULL,
  `nombre` VARCHAR(45) NULL,
  `tarifa` VARCHAR(45) NULL,
  `status` VARCHAR(45) NULL,
  PRIMARY KEY (`idice`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `facturacionSRI`.`irbpnr`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `facturacionSRI`.`irbpnr` (
  `idirbpnr` INT NOT NULL AUTO_INCREMENT,
  `codigoporcentaje` VARCHAR(45) NULL,
  `nombre` VARCHAR(45) NULL,
  `tarifa` VARCHAR(45) NULL,
  `status` VARCHAR(45) NULL,
  PRIMARY KEY (`idirbpnr`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `facturacionSRI`.`producto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `facturacionSRI`.`producto` (
  `idproducto` INT NOT NULL AUTO_INCREMENT,
  `fkemisor` INT NOT NULL,
  `fkiva` INT NULL,
  `fkice` INT NULL,
  `fkirbpnr` INT NULL,
  `nombre` VARCHAR(255) NULL,
  `stock` INT NULL,
  `preciounitario` DECIMAL(10,2) NULL,
  `codprincipal` VARCHAR(25) NULL,
  `codauxiliar` VARCHAR(25) NULL,
  `subcidio` ENUM('ONLINE', 'OFFLINE') NULL,
  `subsidiovalor` DECIMAL(10,2) NULL,
  `tipo` ENUM('BIEN', 'SERVICIO') NULL,
  `status` ENUM('ONLINE', 'OFFLINE') NULL,
  `img1` VARCHAR(255) NULL,
  `img2` VARCHAR(255) NULL,
  `img3` VARCHAR(255) NULL,
  PRIMARY KEY (`idproducto`),
  INDEX `fk_producto_emisor1_idx` (`fkemisor` ASC),
  INDEX `fk_producto_iva1_idx` (`fkiva` ASC),
  INDEX `fk_producto_ice1_idx` (`fkice` ASC),
  INDEX `fk_producto_irbpnr1_idx` (`fkirbpnr` ASC),
  CONSTRAINT `fk_producto_emisor1`
    FOREIGN KEY (`fkemisor`)
    REFERENCES `facturacionSRI`.`emisor` (`idemisor`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_producto_iva1`
    FOREIGN KEY (`fkiva`)
    REFERENCES `facturacionSRI`.`iva` (`idiva`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_producto_ice1`
    FOREIGN KEY (`fkice`)
    REFERENCES `facturacionSRI`.`ice` (`idice`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_producto_irbpnr1`
    FOREIGN KEY (`fkirbpnr`)
    REFERENCES `facturacionSRI`.`irbpnr` (`idirbpnr`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `facturacionSRI`.`cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `facturacionSRI`.`cliente` (
  `idcliente` INT NOT NULL AUTO_INCREMENT,
  `fkemisor` INT NOT NULL,
  `nombre` VARCHAR(255) NULL,
  `tipoidentificacion` VARCHAR(2) NULL,
  `identificacion` VARCHAR(25) NULL,
  `direccion` LONGTEXT NULL,
  `email` VARCHAR(255) NULL,
  `telefono` VARCHAR(255) NULL,
  `created` DATETIME NULL,
  PRIMARY KEY (`idcliente`),
  INDEX `fk_cliente_emisor1_idx` (`fkemisor` ASC),
  CONSTRAINT `fk_cliente_emisor1`
    FOREIGN KEY (`fkemisor`)
    REFERENCES `facturacionSRI`.`emisor` (`idemisor`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `facturacionSRI`.`establecimiento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `facturacionSRI`.`establecimiento` (
  `idestablecimiento` INT NOT NULL AUTO_INCREMENT,
  `fkemisor` INT NOT NULL,
  `nombre` VARCHAR(60) NULL,
  `codigo` VARCHAR(3) NULL,
  `web` VARCHAR(255) NULL,
  `nombrecomercial` VARCHAR(255) NULL,
  `direccion` VARCHAR(300) NULL,
  `email` VARCHAR(300) NULL,
  `status` ENUM('ONLINE', 'OFFLINE') NULL,
  `logo` VARCHAR(200) NULL,
  `proformasecuencia` VARCHAR(200) NULL,
  PRIMARY KEY (`idestablecimiento`),
  INDEX `fk_establecimiento_emisor1_idx` (`fkemisor` ASC),
  CONSTRAINT `fk_establecimiento_emisor1`
    FOREIGN KEY (`fkemisor`)
    REFERENCES `facturacionSRI`.`emisor` (`idemisor`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `facturacionSRI`.`ptoemision`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `facturacionSRI`.`ptoemision` (
  `idptoemision` INT NOT NULL AUTO_INCREMENT,
  `fkemisor` INT NOT NULL,
  `nombre` VARCHAR(60) NULL,
  `codigo` VARCHAR(3) NULL,
  `secuenciafactura` VARCHAR(9) NULL,
  `secuencialiquidacion` VARCHAR(9) NULL,
  `secuencialcredito` VARCHAR(9) NULL,
  `secuencialdebito` VARCHAR(9) NULL,
  `secuencialremision` VARCHAR(9) NULL,
  `secuencialretencion` VARCHAR(9) NULL,
  `status` ENUM('ONLINE', 'OFFLINE') NULL,
  PRIMARY KEY (`idptoemision`),
  INDEX `fk_ptoemision_emisor1_idx` (`fkemisor` ASC),
  CONSTRAINT `fk_ptoemision_emisor1`
    FOREIGN KEY (`fkemisor`)
    REFERENCES `facturacionSRI`.`emisor` (`idemisor`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `facturacionSRI`.`factura`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `facturacionSRI`.`factura` (
  `idfactura` INT NOT NULL AUTO_INCREMENT,
  `fkptoemision` INT NOT NULL,
  `fkestablecimiento` INT NOT NULL,
  `fkcliente` INT NOT NULL,
  `claveacceso` VARCHAR(49) NULL,
  `numautorizacion` VARCHAR(49) NULL,
  `fechaautorizacion` DATETIME NULL,
  `status` ENUM('CREADO', 'NO_AUTORIZADO', 'ANULADA', 'ERROR', 'AUTORIZADO', 'DESPACHADO', 'ENTREGADO') NULL,
  `ambiente` INT NULL,
  `tipoemision` VARCHAR(100) NULL,
  `secuencial` VARCHAR(9) NULL,
  `formapago` VARCHAR(4) NULL,
  `plazo` VARCHAR(6) NULL,
  `fechaemision` DATE NULL,
  `nombrearchivo` VARCHAR(200) NULL,
  `totalsinimpuestos` DECIMAL(10,2) NULL,
  `subtotal` DECIMAL(10,2) NULL,
  `subtotal0` DECIMAL(10,2) NULL,
  `subtotalnoiva` DECIMAL(10,2) NULL,
  `subtotalexcentoiva` DECIMAL(10,2) NULL,
  `valorice` DECIMAL(10,2) NULL,
  `valorirbpn` DECIMAL(10,2) NULL,
  `iva` DECIMAL(10,2) NULL,
  `totaldescuento` DECIMAL(10,2) NULL,
  `propina` DECIMAL(10,2) NULL,
  `valortotal` DECIMAL(10,2) NULL,
  `totalsubsidio` DECIMAL(10,2) NULL,
  `subsidiosiniva` DECIMAL(10,2) NULL,
  `firmado` TINYINT(1) NULL,
  `cargaautimatica` TINYINT(1) NULL,
  `observacion` VARCHAR(300) NULL,
  PRIMARY KEY (`idfactura`),
  INDEX `fk_factura_ptoemision1_idx` (`fkptoemision` ASC),
  INDEX `fk_factura_establecimiento1_idx` (`fkestablecimiento` ASC),
  INDEX `fk_factura_cliente1_idx` (`fkcliente` ASC),
  CONSTRAINT `fk_factura_ptoemision1`
    FOREIGN KEY (`fkptoemision`)
    REFERENCES `facturacionSRI`.`ptoemision` (`idptoemision`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_factura_establecimiento1`
    FOREIGN KEY (`fkestablecimiento`)
    REFERENCES `facturacionSRI`.`establecimiento` (`idestablecimiento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_factura_cliente1`
    FOREIGN KEY (`fkcliente`)
    REFERENCES `facturacionSRI`.`cliente` (`idcliente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `facturacionSRI`.`detallefactura`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `facturacionSRI`.`detallefactura` (
  `iddetallefactura` INT NOT NULL AUTO_INCREMENT,
  `fkproducto` INT NOT NULL,
  `fkfactura` INT NOT NULL,
  `cantidad` INT NULL,
  `nombre` VARCHAR(300) NULL,
  `codigo` VARCHAR(300) NULL,
  `preciounitario` DECIMAL(10,2) NULL,
  `descuento` DECIMAL(10,2) NULL,
  `subtotal` DECIMAL(10,2) NULL,
  `iva` DECIMAL(10,2) NULL,
  `ice` DECIMAL(10,2) NULL,
  PRIMARY KEY (`iddetallefactura`),
  INDEX `fk_detallefactura_factura1_idx` (`fkfactura` ASC),
  INDEX `fk_detallefactura_producto1_idx` (`fkproducto` ASC),
  CONSTRAINT `fk_detallefactura_factura1`
    FOREIGN KEY (`fkfactura`)
    REFERENCES `facturacionSRI`.`factura` (`idfactura`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_detallefactura_producto1`
    FOREIGN KEY (`fkproducto`)
    REFERENCES `facturacionSRI`.`producto` (`idproducto`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `facturacionSRI`.`transportista`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `facturacionSRI`.`transportista` (
  `idtransportista` INT NOT NULL AUTO_INCREMENT,
  `ruc` VARCHAR(45) NULL,
  `nombre` VARCHAR(255) NULL,
  `identificacion` VARCHAR(25) NULL,
  `identificaciontipo` VARCHAR(2) NULL,
  `placa` VARCHAR(10) NULL,
  `direccion` LONGTEXT NULL,
  `celular` VARCHAR(255) NULL,
  `email` VARCHAR(255) NULL,
  `referencia` VARCHAR(255) NULL,
  PRIMARY KEY (`idtransportista`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `facturacionSRI`.`guia`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `facturacionSRI`.`guia` (
  `idguia` INT NOT NULL AUTO_INCREMENT,
  `fkfactura` INT NOT NULL,
  `fktransportista` INT NOT NULL,
  `claveacceso` VARCHAR(45) NULL,
  `numeroautorizacion` VARCHAR(45) NULL,
  `fechaautorizacion` VARCHAR(45) NULL,
  `status` VARCHAR(45) NULL,
  `tipoemision` VARCHAR(45) NULL,
  `secuencial` VARCHAR(45) NULL,
  `nombrearchivo` VARCHAR(45) NULL,
  `partida` VARCHAR(45) NULL,
  `fechainicio` VARCHAR(45) NULL,
  `fechafin` VARCHAR(45) NULL,
  `firmado` VARCHAR(45) NULL,
  `observacion` VARCHAR(45) NULL,
  PRIMARY KEY (`idguia`),
  INDEX `fk_guia_factura1_idx` (`fkfactura` ASC),
  INDEX `fk_guia_transportista1_idx` (`fktransportista` ASC),
  CONSTRAINT `fk_guia_factura1`
    FOREIGN KEY (`fkfactura`)
    REFERENCES `facturacionSRI`.`factura` (`idfactura`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_guia_transportista1`
    FOREIGN KEY (`fktransportista`)
    REFERENCES `facturacionSRI`.`transportista` (`idtransportista`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
