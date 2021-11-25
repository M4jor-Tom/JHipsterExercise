import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'region',
        data: { pageTitle: 'jHipsterExerciseApp.region.home.title' },
        loadChildren: () => import('./region/region.module').then(m => m.RegionModule),
      },
      {
        path: 'country',
        data: { pageTitle: 'jHipsterExerciseApp.country.home.title' },
        loadChildren: () => import('./country/country.module').then(m => m.CountryModule),
      },
      {
        path: 'location',
        data: { pageTitle: 'jHipsterExerciseApp.location.home.title' },
        loadChildren: () => import('./location/location.module').then(m => m.LocationModule),
      },
      {
        path: 'department',
        data: { pageTitle: 'jHipsterExerciseApp.department.home.title' },
        loadChildren: () => import('./department/department.module').then(m => m.DepartmentModule),
      },
      {
        path: 'task',
        data: { pageTitle: 'jHipsterExerciseApp.task.home.title' },
        loadChildren: () => import('./task/task.module').then(m => m.TaskModule),
      },
      {
        path: 'employee',
        data: { pageTitle: 'jHipsterExerciseApp.employee.home.title' },
        loadChildren: () => import('./employee/employee.module').then(m => m.EmployeeModule),
      },
      {
        path: 'job',
        data: { pageTitle: 'jHipsterExerciseApp.job.home.title' },
        loadChildren: () => import('./job/job.module').then(m => m.JobModule),
      },
      {
        path: 'job-history',
        data: { pageTitle: 'jHipsterExerciseApp.jobHistory.home.title' },
        loadChildren: () => import('./job-history/job-history.module').then(m => m.JobHistoryModule),
      },
      {
        path: 'client',
        data: { pageTitle: 'jHipsterExerciseApp.client.home.title' },
        loadChildren: () => import('./client/client.module').then(m => m.ClientModule),
      },
      {
        path: 'commande',
        data: { pageTitle: 'jHipsterExerciseApp.commande.home.title' },
        loadChildren: () => import('./commande/commande.module').then(m => m.CommandeModule),
      },
      {
        path: 'connexion',
        data: { pageTitle: 'jHipsterExerciseApp.connexion.home.title' },
        loadChildren: () => import('./connexion/connexion.module').then(m => m.ConnexionModule),
      },
      {
        path: 'produit',
        data: { pageTitle: 'jHipsterExerciseApp.produit.home.title' },
        loadChildren: () => import('./produit/produit.module').then(m => m.ProduitModule),
      },
      {
        path: 'caracteristique',
        data: { pageTitle: 'jHipsterExerciseApp.caracteristique.home.title' },
        loadChildren: () => import('./caracteristique/caracteristique.module').then(m => m.CaracteristiqueModule),
      },
      {
        path: 'famille',
        data: { pageTitle: 'jHipsterExerciseApp.famille.home.title' },
        loadChildren: () => import('./famille/famille.module').then(m => m.FamilleModule),
      },
      {
        path: 'sous-famille',
        data: { pageTitle: 'jHipsterExerciseApp.sousFamille.home.title' },
        loadChildren: () => import('./sous-famille/sous-famille.module').then(m => m.SousFamilleModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
