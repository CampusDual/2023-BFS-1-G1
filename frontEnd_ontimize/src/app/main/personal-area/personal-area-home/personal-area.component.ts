import { Component, Injector, OnInit, ViewChild } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { OntimizeService, OTableComponent } from 'ontimize-web-ngx';

@Component({
  selector: 'app-personal-area',
  templateUrl: './personal-area.component.html',
  styleUrls: ['./personal-area.component.css']
})
export class PersonalAreaComponent implements OnInit {

  public isAdmin: boolean;
  private myRoleService: OntimizeService;
  public selectedDocument: any;

  @ViewChild('table', {static: false }) public tableDocuments: OTableComponent;


  constructor(
    private router: Router,
    private actRoute: ActivatedRoute,
    public injector: Injector
  ) {
    this.myRoleService = this.injector.get(OntimizeService);
  }

  ngOnInit() {
    this.configureUserRoleService();
    this.myRoleService.query({}, ["rolename"], "myRole").subscribe(
      response => {
        if (response.data && response.data.length) {
          let rol = response.data[0].rolename;
          if (rol == "admin") {
            this.isAdmin = true;
          } else {
            this.isAdmin = false;
          }
        }else{
          this.isAdmin = false;
        }
      },
      error => console.error(error)
    );
  }


  refreshTable(event){
    this.tableDocuments.refresh();
  }

  navigate() {
    this.router.navigate(['../', 'login'], { relativeTo: this.actRoute });
  }

  configureUserRoleService() {
    const conf = this.myRoleService.getDefaultServiceConfiguration("userrole");
    this.myRoleService.configureService(conf);
  } 
    
  onAction1(id: number) {
    this.router.navigate(['/main/personal-area/personal-area-detail/'+id]);


  }

}
