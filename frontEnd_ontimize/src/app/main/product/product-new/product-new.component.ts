import { Component, Injector, OnInit, ViewChild } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material';
import { DialogService, OFileInputComponent, OFormComponent, OTableComponent, OntimizeService } from 'ontimize-web-ngx';

@Component({
  selector: 'app-product-new',
  templateUrl: './product-new.component.html',
  styleUrls: ['./product-new.component.css']
})
export class ProductNewComponent implements OnInit {

  comboVisible = false;
  protected productService: OntimizeService;

   productForm: FormGroup;
   @ViewChild('formproducts', { static: false }) formProducts: OFormComponent;
   @ViewChild('fileinput',{static:true}) fileInput:OFileInputComponent;
   @ViewChild('filetable',{static:true}) fileTable:OTableComponent;

   constructor(private formBuilder: FormBuilder, protected dialog: MatDialog,protected dialogService: DialogService,public injector: Injector) {
    this.productService = this.injector.get(OntimizeService);
   }
   
  ngOnInit() {
    const conf = this.productService.getDefaultServiceConfiguration('products');
    this.productService.configureService(conf);
  }

    getFileData(){
      if(this.formProducts){
      return {product_id:this.formProducts.getDataValue('id')};
    }else{
      return {};
    }
    }
  
    onUploadFiles(event){
      if(event.response){
        let data = event.response.data;
        let errors:string[]= data.filter(element => element.status != "OK" && element.status != "ALREADY_EXIST").map(element => {return element.name});
        let oks:string[] = data.filter(element => element.status === "OK").map(element => {return element.name});
        let exists:string[]= data.filter(element => element.status === "ALREADY_EXIST").map(element => {return element.name});
        let message:string = '<p><b>Resultado de la subida de documentos:</b></p>';
        if(oks.length>0){
          message+='<p>Ficheros subidos correctamente: <ul><li>'+oks.join("</li><li>")+"</li></ul></p>";
        }
        if(exists.length>0){
          message+='<p>Ficheros que no se han subido por que ya existen: <ul><li>'+exists.join("</li><li>")+"</li></ul></p>";
        }
        if(errors.length>0){
          message+='<p>Ficheros que generaron un error en la subida: <ul><li>'+errors.join("</li><li>")+"</li></ul></p>";
        }
        if (this.dialogService) {
          if(errors.length ===0){
            this.dialogService.info('Resultado',message);
          }else{
            this.dialogService.error('Resultado',message);
          }
          
        }
      }
      this.fileInput.clearValue();
      this.fileTable.refresh();
    }
  
    actionClick(event){
      this.productService.query({id:event.id}, ['name','base64'], 'fileContent').subscribe(res => {
        if (res.data && res.data.length) {
          let filename = res.data[0].name;
          let base64 = res.data[0].base64;
          const src = `data:text/csv;base64,${base64}`;
          const link = document.createElement("a");
          link.href = src;
          link.download = filename;
          link.click();
          link.remove();
        }
      });
      
    }
  
    onError(event){
      if(event.status === 507){
        this.showError(event);
      }
      this.fileInput.clearValue();
    }
  
    showError(event: any) {
      if (this.dialogService) {
      this.dialogService.error('Error',
          'El fichero ya existe');
      }
    }





  onSave() {
    if (this.productForm.valid) {
      console.log('Datos válidos, guardando en la base de datos...');
    } else {
      console.log('Datos inválidos, no se puede guardar en la base de datos.');
    }
  }
}



