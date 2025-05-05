import {Component, Input} from '@angular/core';
import {Observable} from 'rxjs';
import {LoadingService} from '../services/loading.service';

@Component({
  selector: 'app-loading-spinner',
  standalone: false,
  templateUrl: './loading-spinner.component.html',
  styleUrl: './loading-spinner.component.css'
})
export class LoadingSpinnerComponent {
  isLoading$: Observable<boolean>;
  @Input() diameter: number = 40;

  constructor(private loadingService: LoadingService) {
    this.isLoading$ = this.loadingService.isLoading$;
  }
}
