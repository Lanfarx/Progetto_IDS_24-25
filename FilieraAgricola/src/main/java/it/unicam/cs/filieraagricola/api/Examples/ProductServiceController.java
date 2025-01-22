package it.unicam.cs.filieraagricola.api.Examples;

import jakarta.websocket.server.PathParam;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(
        origins = {"http://localhost:63342"}
)
public class ProductServiceController {
    @Autowired
    private ProductListRepository productRepository;

    @Autowired
    public ProductServiceController(ProductListRepository productRepository) {
        Product miele = new Product();
        miele.setName("Miele");
        miele.setId("0");
        miele.setPrice(10.0);
        productRepository.save(miele);
        Product zucchero = new Product();
        zucchero.setName("Zucchero");
        zucchero.setId("1");
        zucchero.setPrice(20.0);
        productRepository.save(zucchero);
    }

    @RequestMapping({"/products"})
    public ResponseEntity<Object> getProducts() {
        return new ResponseEntity(this.productRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping({"/product/{id}"})
    public ResponseEntity<Object> getProduct(@PathVariable("id") String id) {
        if (!this.productRepository.existsById(id)) {
            throw new ProductNotFoundException();
        } else {
            return new ResponseEntity(this.productRepository.findById(id), HttpStatus.OK);
        }
    }

    @GetMapping({"/product"})
    public ResponseEntity<Object> getProductParam(@PathParam("id") String id) {
        if (!this.productRepository.existsById(id)) {
            throw new ProductNotFoundException();
        } else {
            return new ResponseEntity(this.productRepository.findById(id), HttpStatus.OK);
        }
    }

    @PostMapping({"/addproduct"})
    public ResponseEntity<Object> addProduct(@RequestBody Product product) {
        if (!this.productRepository.existsById(product.getId())) {
            this.productRepository.save(product);
            return new ResponseEntity("Product Created", HttpStatus.CREATED);
        } else {
            return new ResponseEntity("Product Already Exists", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping({"addproduct"})
    public ResponseEntity<Object> addProductWithParam(@PathParam("id") String id, @PathParam("name") String name, @PathParam("price") double price) {
        if (!this.productRepository.existsById(id)) {
            Product product = new Product();
            product.setId(id);
            product.setName(name);
            product.setPrice(price);
            this.productRepository.save(product);
            return new ResponseEntity("Product Created", HttpStatus.CREATED);
        } else {
            return new ResponseEntity("Product Already Exists", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping({"/product/{id}"})
    public ResponseEntity<Object> deleteProduct(@PathVariable("id") String id) {
        this.productRepository.deleteById(id);
        return new ResponseEntity("Product " + id + " Deleted", HttpStatus.OK);
    }

    @RequestMapping(
            value = {"/update"},
            method = {RequestMethod.PUT}
    )
    public ResponseEntity<Object> updateProduct(@PathParam("id") String id, @RequestBody Product product) {
        if (this.productRepository.existsById(id)) {
            this.productRepository.save(product);
            return new ResponseEntity("Product " + id + " Updated", HttpStatus.OK);
        } else {
            return new ResponseEntity("Product " + id + " Not Found", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(
            value = {"/fileUpload"},
            method = {RequestMethod.POST},
            consumes = {"multipart/form-data"}
    )
    public ResponseEntity<Object> fileUpload(@RequestParam("file") MultipartFile file, @RequestParam("fileName") String filename) {
        File uploadedFile = new File("src/main/resources/" + filename);

        try {
            uploadedFile.createNewFile();
            FileOutputStream fileStream = new FileOutputStream(uploadedFile);
            fileStream.write(file.getBytes());
            fileStream.close();
        } catch (FileNotFoundException var5) {
            FileNotFoundException e = var5;
            throw new RuntimeException(e);
        } catch (IOException var6) {
            IOException e = var6;
            throw new RuntimeException(e);
        }

        return new ResponseEntity("File uploaded", HttpStatus.OK);
    }

    @GetMapping({"/download"})
    public ResponseEntity<Object> fileDownload(@PathParam("filename") String filename) throws FileNotFoundException {
        String fileN = "src/main/resources/" + filename;
        File file = new File(fileN);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        HttpHeaders header = new HttpHeaders();
        header.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
        header.add("Cache-control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        ResponseEntity<Object> response = ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(header)).contentLength(file.length()).contentType(MediaType.parseMediaType("application/txt")).body(resource);
        return response;
    }
}
