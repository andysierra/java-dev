SELECT * FROM franchise.franquicia;
SELECT * FROM franchise.producto;
SELECT * FROM franchise.sucursal;
SELECT * FROM franchise.sucursal_x_producto;

TRUNCATE TABLE franchise.franquicia RESTART IDENTITY CASCADE;
TRUNCATE TABLE franchise.sucursal RESTART IDENTITY CASCADE;
TRUNCATE TABLE franchise.producto RESTART IDENTITY CASCADE;
TRUNCATE TABLE franchise.sucursal_x_producto RESTART IDENTITY CASCADE;

