package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import model.Producto;

public class apachebook {

	public static final String RUTA_DESCARGAS = System.getProperty("user.home") + File.separator + "Downloads";

	public static void createExcelFormat(File archivoDestino) {
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet hoja = workbook.createSheet("Personas");

			Row encabezado = hoja.createRow(0);
			encabezado.createCell(0).setCellValue("Referencia");
			encabezado.createCell(1).setCellValue("Nombre");
			encabezado.createCell(2).setCellValue("Precio");
			encabezado.createCell(3).setCellValue("Cantidad");

			// Autoajustar ancho de columnas
			hoja.autoSizeColumn(0);
			hoja.autoSizeColumn(1);
			hoja.autoSizeColumn(2);
			hoja.autoSizeColumn(3);

			try (FileOutputStream fos = new FileOutputStream(archivoDestino)) {
				workbook.write(fos);
				System.out.println("✅ Plantilla creada correctamente en: " + archivoDestino.getAbsolutePath());
			}

		} catch (IOException e) {
			System.err.println("❌ Error al crear plantilla: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static ArrayList<Producto> fetchExcel(File archivoOrigen) {
		ArrayList<Producto> productos = new ArrayList<>();

		try (FileInputStream fis = new FileInputStream(archivoOrigen); Workbook workbook = new XSSFWorkbook(fis)) {

			Sheet hoja = workbook.getSheetAt(0);

			for (Row fila : hoja) {
				if (fila.getRowNum() == 0) continue; // Saltar encabezado

				Cell cellReferencia = fila.getCell(0);
				Cell cellNombre = fila.getCell(1);
				Cell cellPrecio = fila.getCell(2);
				Cell cellCantidad = fila.getCell(3);

				if (cellReferencia == null || cellNombre == null || cellPrecio == null || cellCantidad == null) continue;

				int referencia = (int) cellReferencia.getNumericCellValue();
				String nombre = cellNombre.getStringCellValue();
				double precio = (double) cellPrecio.getNumericCellValue();
				int cantidad = (int) cellCantidad.getNumericCellValue();

				productos.add(new Producto(referencia, nombre, precio, cantidad));
			}

		} catch (IOException e) {
			System.err.println("❌ Error al leer archivo Excel: " + e.getMessage());
			e.printStackTrace();
		}
		return productos;
	}
}