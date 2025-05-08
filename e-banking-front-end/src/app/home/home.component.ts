import { Component } from '@angular/core';
import {StatsService} from '../services/stats.service';
import {ChartConfiguration} from 'chart.js';
import {BehaviorSubject} from 'rxjs';

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  numberOfAccounts = 0;
  numberOfCustomers = 0;
  totalOperations = 0;
  totalAmount = 0;

  isLoading$ = new BehaviorSubject<boolean>(true);

  diameter = 100;

  pieChartType: any = 'pie';
  pieChartLabels = ['Savings', 'Current', 'Blocked'];
  pieChartData = {
    labels: this.pieChartLabels,
    datasets: [
      {
        data: [0, 0, 0], // Dynamique si besoin
        backgroundColor: ['#42A5F5', '#66BB6A', '#FFA726'],
      }
    ]
  };

  pieChartOptions = { responsive: true, plugins: { legend: {} } };

  barChartData = {
    labels: ['Day 1', 'Day 2', 'Day 3', 'Day 4', 'Day 5'],
    datasets: [
      {
        label: 'Debit',
        data: [0, 0, 0, 0, 0],
        backgroundColor: 'rgba(255, 99, 132, 0.5)',
        borderColor: 'rgba(255, 99, 132, 1)',
        borderWidth: 1
      },
      {
        label: 'Credit',
        data: [0, 0, 0, 0, 0],
        backgroundColor: 'rgba(54, 162, 235, 0.5)',
        borderColor: 'rgba(54, 162, 235, 1)',
        borderWidth: 1
      },
      {
        label: 'Transfer',
        data: [0, 0, 0, 0, 0],
        backgroundColor: 'rgba(75, 192, 192, 0.5)',
        borderColor: 'rgba(75, 192, 192, 1)',
        borderWidth: 1
      }
    ]
  };

  barChartOptions: ChartConfiguration<'bar'>['options'] = {
    responsive: true,
    scales: {
      x: { title: { display: true, text: 'Days' } },
      y: { title: { display: true, text: 'Amount ($)' } }
    },
    plugins: {
      legend: {
        position: 'top' as const // 👈 Solution
      }
    }
  };

  constructor(private statsService: StatsService) {}

  ngOnInit(): void {
    const today = new Date();
    const last7Days = new Date();
    last7Days.setDate(today.getDate() - 7);

    const formatDate = (date: Date) => date.toISOString().split('T')[0];

    const startDate = formatDate(last7Days);
    const endDate = formatDate(today);

    this.statsService.getDashboardStats(startDate, endDate)
      .subscribe({
        next: data => {
          this.numberOfAccounts = data.numberOfAccounts || 0;
          this.numberOfCustomers = data.numberOfCustomers || 0;
          this.totalOperations = data.totalOperations || 0;
          this.totalAmount = data.totalAmount || 0;
        },
        error: err => console.error('Erreur stats dashboard', err),
        complete: () => this.isLoading$.next(false)
      });

    this.statsService.getOperationsChartData(startDate, endDate)
      .subscribe({
        next: chartData => {
          const labels = Object.keys(
            chartData.debit || chartData.credit || chartData.transfer || {}
          ).sort();
          console.log(chartData)

          this.barChartData.labels = labels;

          this.barChartData.datasets[0].data = labels.map(label => chartData.debit?.[label] || 0);
          this.barChartData.datasets[1].data = labels.map(label => chartData.credit?.[label] || 0);
          this.barChartData.datasets[2].data = labels.map(label => chartData.transfer?.[label] || 0);
        },
        error: err => console.error('Erreur données graphique', err)
      });
  }
}
