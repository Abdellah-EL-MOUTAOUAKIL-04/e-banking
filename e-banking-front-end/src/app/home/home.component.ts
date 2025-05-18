import { Component, OnInit } from '@angular/core';
import { StatsService } from '../services/stats.service';
import { ChartConfiguration } from 'chart.js';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  standalone: false,
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  numberOfAccounts = 0;
  numberOfCustomers = 0;
  totalOperations = 0;
  totalAmount = 0;

  isLoading$ = new BehaviorSubject<boolean>(true);
  diameter = 100;

  // ✅ Pie Chart
  pieChartLabels: string[] = ['Savings', 'Current'];
  pieChartData: ChartConfiguration<'pie'>['data'] = {
    labels: ['Savings', 'Current','Blocked'],
    datasets: [
      {
        data: [0, 0],
        backgroundColor: ['#42A5F5', '#66BB6A'],
      }
    ]
  };

  pieChartOptions: ChartConfiguration<'pie'>['options'] = {
    responsive: true,
    plugins: {
      legend: {
        position: 'top'
      }
    }
  };

  // ✅ Bar Chart
  barChartData: ChartConfiguration<'bar'>['data'] = {
    labels: [],
    datasets: [
      {
        label: 'Debit',
        data: [],
        backgroundColor: 'rgba(255, 99, 132, 0.5)',
        borderColor: 'rgba(255, 99, 132, 1)',
        borderWidth: 1
      },
      {
        label: 'Credit',
        data: [],
        backgroundColor: 'rgba(54, 162, 235, 0.5)',
        borderColor: 'rgba(54, 162, 235, 1)',
        borderWidth: 1
      },
      {
        label: 'Transfer',
        data: [],
        backgroundColor: 'rgba(75, 192, 192, 0.5)',
        borderColor: 'rgba(75, 192, 192, 1)',
        borderWidth: 1
      }
    ]
  };

  barChartOptions: ChartConfiguration<'bar'>['options'] = {
    responsive: true,
    scales: {
      x: { title: { display: true, text: 'Date' } },
      y: { title: { display: true, text: 'Amount ($)' } }
    },
    plugins: {
      legend: { position: 'top' }
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

    // ✅ General Stats
    this.statsService.getDashboardStats(startDate, endDate).subscribe({
      next: data => {
        this.numberOfAccounts = data.numberOfAccounts || 0;
        this.numberOfCustomers = data.numberOfCustomers || 0;
        this.totalOperations = data.totalOperations || 0;
        this.totalAmount = data.totalAmount || 0;
        console.log("Data : ",data);
      },
      error: err => console.error('Error fetching dashboard stats', err),
      complete: () => this.isLoading$.next(false)
    });

    // ✅ Bar Chart Data
    this.statsService.getOperationsChartData(startDate, endDate).subscribe({
      next: chartData => {
        const labelsSet = new Set([
          ...Object.keys(chartData.debit || {}),
          ...Object.keys(chartData.credit || {}),
          ...Object.keys(chartData.transfer || {})
        ]);

        const labels = Array.from(labelsSet).sort();

        this.barChartData = {
          labels,
          datasets: [
            {
              label: 'Debit',
              data: labels.map(label => chartData.debit?.[label] || 0),
              backgroundColor: 'rgba(255, 99, 132, 0.5)',
              borderColor: 'rgba(255, 99, 132, 1)',
              borderWidth: 1
            },
            {
              label: 'Credit',
              data: labels.map(label => chartData.credit?.[label] || 0),
              backgroundColor: 'rgba(54, 162, 235, 0.5)',
              borderColor: 'rgba(54, 162, 235, 1)',
              borderWidth: 1
            },
            {
              label: 'Transfer',
              data: labels.map(label => chartData.transfer?.[label] || 0),
              backgroundColor: 'rgba(75, 192, 192, 0.5)',
              borderColor: 'rgba(75, 192, 192, 1)',
              borderWidth: 1
            }
          ]
        };
      },
      error: err => console.error('Error fetching operations data', err)
    });

    // ✅ Pie Chart Data from Account Types
    this.statsService.getAccountsByType().subscribe({
      next: typeData => {
        const savings = typeData['SavingAccount'] || 0;
        const current = typeData['CurrentAccount'] || 0;

        this.pieChartData = {
          labels: ['Savings', 'Current'],
          datasets: [
            {
              data: [savings, current],
              backgroundColor: ['#42A5F5', '#66BB6A'],
            }
          ]
        };
      },
      error: err => console.error('Error fetching account types', err)
    });
  }
}
