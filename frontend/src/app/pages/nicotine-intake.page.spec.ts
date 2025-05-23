import { TestBed } from '@angular/core/testing';
import { NicotineIntakePageComponent } from './nicotine-intake.page';
import { provideHttpClient } from '@angular/common/http';
import {
  ProizvodControllerService,
  UnosNikotinaControllerService,
} from '../api';
import { of, throwError } from 'rxjs';

class MockProizvodControllerService {
  sviProizvodi() {
    return of([
      { idProizvod: 1, opis: 'Test Proizvod', nikotinSadrzaj: 5 },
      { idProizvod: 2, opis: 'Drugi Proizvod', nikotinSadrzaj: 10 },
    ]);
  }
}
class MockUnosNikotinaControllerService {
  batchUnosNikotina() {
    return of({});
  }
}

describe('NicotineIntakePageComponent', () => {
  let component: NicotineIntakePageComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NicotineIntakePageComponent],
      providers: [
        provideHttpClient(),
        {
          provide: ProizvodControllerService,
          useClass: MockProizvodControllerService,
        },
        {
          provide: UnosNikotinaControllerService,
          useClass: MockUnosNikotinaControllerService,
        },
      ],
    }).compileComponents();
    const fixture = TestBed.createComponent(NicotineIntakePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch products on init', () => {
    expect(component.productsList.length).toBeGreaterThan(0);
  });

  it('should add a product entry', () => {
    const initialLength = component.products.length;
    component.addProduct();
    expect(component.products.length).toBe(initialLength + 1);
  });

  it('should remove a product entry', () => {
    component.addProduct();
    const initialLength = component.products.length;
    component.removeProduct(0);
    expect(component.products.length).toBe(initialLength - 1);
  });

  it('should update product and quantity', () => {
    component.addProduct();
    component.onProductChange(0, { target: { value: '1' } } as any);
    component.onQuantityChange(0, { target: { value: '3' } } as any);
    expect(component.products[0].productId).toBe(1);
    expect(component.products[0].quantity).toBe(3);
  });

  it('should calculate totalQuantity correctly', () => {
    component.productsList = [
      { idProizvod: 1, opis: 'Test Proizvod', nikotinSadrzaj: 5 },
      { idProizvod: 2, opis: 'Drugi Proizvod', nikotinSadrzaj: 10 },
    ];
    component.products = [
      { productId: 1, quantity: 2 },
      { productId: 2, quantity: 1 },
    ];
    component.totalQuantity = component.products.reduce((sum, p) => {
      const prod = component.productsList.find(
        (pr) => pr.idProizvod === p.productId
      );
      return (
        sum +
        (prod ? Number(p.quantity || 0) * Number(prod.nikotinSadrzaj || 0) : 0)
      );
    }, 0);
    expect(component.totalQuantity).toBe(20);
  });

  it('should set isSaved to true on successful submit', () => {
    component.productsList = [
      { idProizvod: 1, opis: 'Test Proizvod', nikotinSadrzaj: 5 },
    ];
    component.products = [{ productId: 1, quantity: 2 }];
    component.date = '2025-05-23';
    component.submit();
    expect(component.isSaved).toBeTrue();
    expect(component.isError).toBeFalse();
  });

  it('should set isError to true on failed submit', () => {
    const fixture = TestBed.createComponent(NicotineIntakePageComponent);
    const comp = fixture.componentInstance;
    const errorService = TestBed.inject(UnosNikotinaControllerService);
    spyOn(errorService, 'batchUnosNikotina').and.returnValue(
      throwError(() => new Error('fail'))
    );
    comp.productsList = [
      { idProizvod: 1, opis: 'Test Proizvod', nikotinSadrzaj: 5 },
    ];
    comp.products = [{ productId: 1, quantity: 2 }];
    comp.date = '2025-05-23';
    comp.submit();
    expect(comp.isError).toBeTrue();
    expect(comp.isSaved).toBeFalse();
  });

  it('should update totalQuantity when products or quantities change', () => {
    component.productsList = [
      { idProizvod: 1, opis: 'Test Proizvod', nikotinSadrzaj: 5 },
      { idProizvod: 2, opis: 'Drugi Proizvod', nikotinSadrzaj: 10 },
    ];
    component.products = [
      { productId: 1, quantity: 2 },
      { productId: 2, quantity: 3 },
    ];
    component.products$.next(component.products);
    expect(component.totalQuantity).toBe(2 * 5 + 3 * 10);
  });
});
