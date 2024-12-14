import fitz  # PyMuPDF
import os

# Abre el archivo PDF
pdf_document = "D:\\Grado Superior\\2.Segundo\\AccesoADatos\\ProyectoFinal\\Documentos\\Documentacion.pdf"
doc = fitz.open(pdf_document)

# Crea un archivo Markdown en la carpeta "ProyectoFinal"
markdown_file = "D:\\Grado Superior\\2.Segundo\\AccesoADatos\\ProyectoFinal\\README.md"
with open(markdown_file, "w", encoding="utf-8") as md_file:
    for page_num in range(len(doc)):
        page = doc.load_page(page_num)
        text = page.get_text("text")
        md_file.write(f"# Página {page_num + 1}\n\n")
        md_file.write(text)
        md_file.write("\n\n")

        # Extrae las imágenes
        image_list = page.get_images(full=True)
        for img_index, img in enumerate(image_list):
            xref = img[0]
            base_image = doc.extract_image(xref)
            image_bytes = base_image["image"]
            image_ext = base_image["ext"]
            image_filename = f"image_page{page_num + 1}_{img_index}.{image_ext}"
            image_path = os.path.join("D:\\Grado Superior\\2.Segundo\\AccesoADatos\\ProyectoFinal\\Documentos", image_filename)

            # Guarda la imagen
            with open(image_path, "wb") as img_file:
                img_file.write(image_bytes)

            # Añade la imagen al archivo Markdown
            md_file.write(f"![Imagen {img_index + 1}](./Documentos/{image_filename})\n\n")

print(f"El archivo PDF ha sido convertido a Markdown y guardado en {markdown_file}")
