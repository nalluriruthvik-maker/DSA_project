package p1;
import java.util.Scanner;

class HospitalRecord {
    int id;
    String name;
    int exp;         
    String details;  

    HospitalRecord next, prev; 

    HospitalRecord(int id, String name, int exp) {
        this.id = id;
        this.name = name;
        this.exp = exp;
    }

    HospitalRecord(int id, String name, String details) {
        this.id = id;
        this.name = name;
        this.details = details;
    }

    HospitalRecord(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        if (exp > 0) {
            return "ID: " + id + " | Dr. " + name + " | Exp: " + exp + " yrs";
        } else if (name != null) {
            return "ID: " + id + " | Name: " + name + " | Condition: " + details;
        } else {
            return details; 
        }
    }
}
class PatientQueue {
    int Front, Rear, size;
    HospitalRecord[] Q;

    PatientQueue() {
        Front = -1; 
        Rear = -1;
        size = 10; 
        Q = new HospitalRecord[size];
    }

    boolean IsFull() {
        if (Rear == size - 1) return true;
        else return false;
    }

    boolean IsEmpty() {
        if ((Front == -1 && Rear == -1) || (Front == Rear + 1)) return true;
        else return false;
    }

    void Enqueue(HospitalRecord E) {
        Q[++Rear] = E;
        if (Front == -1) Front++;
    }

    HospitalRecord Dequeue() {
        HospitalRecord E = Q[Front];
        Q[Front++] = null; 
        return E;
    }

    void Display() {
        for (int i = Front; i <= Rear; i++) {
            if (Q[i] != null) {
                System.out.println(Q[i]);
            }
        }
    }
}

class ExpressionStack {
    int Top, size;
    char[] stack;

    ExpressionStack(int size) {
        this.size = size;
        Top = -1;
        stack = new char[size];
    }

    boolean IsEmpty() {
        if (Top == -1) return true;
        else return false;
    }

    boolean IsFull() {
        if (Top == size - 1) return true;
        else return false;
    }

    void Push(char x) {
        stack[++Top] = x;
    }

    char Pop() {
        return stack[Top--];
    }

    char Peek() {
        return stack[Top];
    }
}

class HospitalOperations {

    int MAX_DOCTORS = 50;
    HospitalRecord[] doctorsArray = new HospitalRecord[MAX_DOCTORS];
    int doctorCount = 0;

    HospitalRecord patientHead = null, patientTail = null;
    int patientCount = 0;

    HospitalRecord historyHead = null, historyTail = null;
    
    PatientQueue waitingRoom = new PatientQueue();

    Scanner sc = new Scanner(System.in);
    public void addPatientToQueue() {
        if (waitingRoom.IsFull()) {
            System.out.println("Waiting Room Queue is Overflowing!");
            return;
        }
        System.out.print("Enter Patient ID: ");
        int id = sc.nextInt(); sc.nextLine(); 
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Condition: ");
        String cond = sc.nextLine();

        HospitalRecord newPatient = new HospitalRecord(id, name, cond);
        waitingRoom.Enqueue(newPatient);
        System.out.println(name + " has been added to the waiting queue.");
    }

    public void admitPatientFromQueue() {
        if (waitingRoom.IsEmpty()) {
            System.out.println("No patients in the queue to admit.");
            return;
        }

        HospitalRecord admittedPatient = waitingRoom.Dequeue();

        admittedPatient.next = patientHead;
        if (patientHead != null) patientHead.prev = admittedPatient;
        else patientTail = admittedPatient; 
        patientHead = admittedPatient;
        patientCount++;

        HospitalRecord historyNode = new HospitalRecord("Admitted: " + admittedPatient.name);
        if (historyHead == null) {
            historyHead = historyTail = historyNode;
        } else {
            historyTail.next = historyNode;
            historyNode.prev = historyTail;
            historyTail = historyNode;
        }

        System.out.println(admittedPatient.name + " was dequeued and admitted!");
    }

    public void displayQueue() {
        System.out.println("\n--- Waiting Queue ---");
        if (waitingRoom.IsEmpty()) {
            System.out.println("Queue is Empty.");
        } else {
            waitingRoom.Display();
        }
    }
    public void deletePatient() {
        if (patientHead == null) {
            System.out.println("No patients currently admitted to delete.");
            return;
        }
        System.out.print("Enter Patient ID to delete (discharge): ");
        int targetId = sc.nextInt(); sc.nextLine();

        HospitalRecord current = patientHead;
        boolean found = false;

        while (current != null) {
            if (current.id == targetId) {
                
                addHistory("Discharged/Deleted: " + current.name);
                
                if (current == patientHead) {
                    patientHead = current.next;
                    if (patientHead != null) patientHead.prev = null;
                    else patientTail = null; 
                } else if (current == patientTail) {
                    patientTail = current.prev;
                    if (patientTail != null) patientTail.next = null;
                } else {
                    current.prev.next = current.next;
                    current.next.prev = current.prev;
                }
                patientCount--;
                found = true;
                System.out.println("Patient " + current.name + " deleted successfully.");
                break;
            }
            current = current.next;
        }
        if (!found) System.out.println("Patient ID not found.");
    }

    public void showPatients() {
        System.out.println("\n--- Admitted Patients ---");
        HospitalRecord current = patientHead;
        if (current == null) System.out.println("No patients currently admitted.");
        while (current != null) {
            System.out.println(current);
            current = current.next;
        }
    }

    public void addHistory(String record) {
        HospitalRecord newNode = new HospitalRecord(record);
        if (historyHead == null) {
            historyHead = historyTail = newNode;
        } else {
            historyTail.next = newNode;
            newNode.prev = historyTail;
            historyTail = newNode;
        }
    }

    public void viewHistory() {
        System.out.println("\n--- Patient Visit History ---");
        HospitalRecord current = historyHead;
        if (current == null) System.out.println("No visits recorded yet.");
        while (current != null) {
            System.out.println("- " + current);
            current = current.next;
        }
    }

    public void addDoctor() {
        if (doctorCount >= MAX_DOCTORS) {
            System.out.println("Doctor Array Full!");
            return;
        }
        System.out.print("Enter ID: ");
        int id = sc.nextInt(); sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Experience (in years): ");
        int exp = sc.nextInt(); sc.nextLine();

        doctorsArray[doctorCount] = new HospitalRecord(id, name, exp);
        doctorCount++;
        System.out.println("Doctor Added!");
    }

    public void deleteDoctor() {
        if (doctorCount == 0) {
            System.out.println("No doctors to delete.");
            return;
        }
        System.out.print("Enter Doctor ID to delete: ");
        int targetId = sc.nextInt(); sc.nextLine();

        int index = -1;
        for (int i = 0; i < doctorCount; i++) {
            if (doctorsArray[i].id == targetId) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            for (int i = index; i < doctorCount - 1; i++) {
                doctorsArray[i] = doctorsArray[i + 1];
            }
            doctorsArray[doctorCount - 1] = null; 
            doctorCount--; 
            System.out.println("Doctor deleted successfully.");
        } else {
            System.out.println("Doctor ID not found.");
        }
    }

    public void showDoctorsSorted() {
        if (doctorCount == 0) {
            System.out.println("No doctors.");
            return;
        }
        HospitalRecord[] temp = new HospitalRecord[doctorCount];
        for (int i = 0; i < doctorCount; i++) temp[i] = doctorsArray[i];
        
        MergeSort(temp, 0, doctorCount - 1);
        
        System.out.println("\n--- Doctors (Sorted by Exp) ---");
        for (int i = 0; i < doctorCount; i++) System.out.println(temp[i]);
    }

    public void searchDoctor() {
        if (doctorCount == 0) {
            System.out.println("No doctors in system.");
            return;
        }
        System.out.print("Enter ID to search: ");
        int id = sc.nextInt(); sc.nextLine();

        QuickSort(doctorsArray, 0, doctorCount - 1);
        int idx = BinarySearch(doctorsArray, id, 0, doctorCount - 1); 
        
        if (idx != -1) System.out.println("Found: " + doctorsArray[idx]);
        else System.out.println("Not found.");
    }

    boolean IsOperator(char x) {
        if (x == '+' || x == '-' || x == '*' || x == '/' || x == '^') return true;
        else return false;
    }

    boolean IsOperand(char x) {
        if ((x >= 'a' && x <= 'z') || (x >= 'A' && x <= 'Z') || (x >= '0' && x <= '9')) return true;
        else return false;
    }

    int GetPrecedence(char x) {
        if (x == '+' || x == '-') return 1;
        else if (x == '*' || x == '/') return 2;
        else if (x == '^') return 3;
        else return 0;
    }

    public void validatePrescription() {
    	Scanner sc=new Scanner(System.in);
        System.out.println("Enter Prescription Formula (e.g., a+b*(c^d-e)): ");
        String exp = sc.nextLine();
        
        ExpressionStack obj1 = new ExpressionStack(exp.length());
        boolean valid = true;
        
        for (char C : exp.toCharArray()) {
            if (C == '(' || C == '[' || C == '{') {
                obj1.Push(C);
            } else if (C == ')' || C == ']' || C == '}') {
                if (obj1.IsEmpty()) {
                    valid = false;
                    break;
                }
                char x = obj1.Pop();
                if ((C == ')' && x != '(') ||
                    (C == ']' && x != '[') ||
                    (C == '}' && x != '{')) {
                    valid = false;
                    break;
                }
            }
        }
        if (!obj1.IsEmpty()) {
            valid = false;
        }
        
        if (!valid) {
            System.out.println("Brackets are not Balanced! Formula rejected.");
            return; 
        }
        
        System.out.println("✅ Brackets are Balanced!");
        
        ExpressionStack obj2 = new ExpressionStack(exp.length());
        String postfix = "";
        
        for (char C : exp.toCharArray()) {
            if (IsOperand(C)) {
                postfix += C;
            } else if (C == '(') {
                obj2.Push(C);
            } else if (IsOperator(C)) {
                while (!obj2.IsEmpty() && GetPrecedence(C) <= GetPrecedence(obj2.Peek())) {
                    postfix += obj2.Pop();
                }
                obj2.Push(C);
            } else if (C == ')') {
                while (obj2.Peek() != '(') {
                    postfix += obj2.Pop();
                }
                obj2.Pop(); 
            }
        }
        
        while (!obj2.IsEmpty()) {
            postfix += obj2.Pop();
        }
        
        System.out.println("Infix Expression is: " + exp);
        System.out.println("Postfix Expression is: " + postfix);
    }
    public void MergeSort(HospitalRecord[] A, int LI, int RI) {
        int MI = (LI + RI) / 2;
        if(LI < RI) {
            MergeSort(A, LI, MI);
            MergeSort(A, MI + 1, RI);
            Copy_Sort_Combine(A, LI, RI, MI);
        }
    }

    public void Copy_Sort_Combine(HospitalRecord[] A, int LI, int RI, int MI) {
        int N1, N2, i, j, k = 0;
        N1 = MI + 1 - LI;
        N2 = RI - MI;

        HospitalRecord[] LA = new HospitalRecord[N1];
        HospitalRecord[] RA = new HospitalRecord[N2];

        for(i = 0; i < N1; i++)
            LA[i] = A[LI + i];
        for(j = 0; j < N2; j++)
            RA[j] = A[MI + 1 + j];

        i = 0; j = 0; k = LI;
        
        while(i < N1 && j < N2) {
            if(LA[i].exp > RA[j].exp) 
                A[k++] = LA[i++];
            else
                A[k++] = RA[j++];
        }

        while(i < N1)
            A[k++] = LA[i++];
        while(j < N2)
            A[k++] = RA[j++];
    }

    public void QuickSort(HospitalRecord[] A, int LI, int RI) {
        int j;
        if(LI < RI) {
            j = Divide(A, LI, RI);
            QuickSort(A, LI, j - 1);
            QuickSort(A, j + 1, RI);
        }
    }

    public int Divide(HospitalRecord[] A, int LI, int RI) {
        int Pivot, j, i;
        HospitalRecord Temp; 
        
        Pivot = A[LI].id;
        i = LI;
        j = RI + 1;
        
        while(i < j) {
            do {
                i++;
            } while(i <= RI && A[i].id < Pivot); 
            
            do {
                j--;
            } while(A[j].id > Pivot);
            
            if(i < j) {
                Temp = A[i];
                A[i] = A[j];
                A[j] = Temp;
            }
        }
        
        Temp = A[LI];
        A[LI] = A[j];
        A[j] = Temp;
        
        return j;
    }

    public int BinarySearch(HospitalRecord[] A, int value, int LI, int RI) {
        while (LI <= RI) {
            int MI = (LI + RI) / 2;
            if (value == A[MI].id) {
                return MI;
            } else if (value < A[MI].id) {
                RI = MI - 1;
            } else if (value > A[MI].id) {
                LI = MI + 1;
            }
        }
        return -1;
    }
}
public class HospitalMangementSystem {

    public static void main(String[] args) {
        
        HospitalOperations obj1 = new HospitalOperations();

        obj1.doctorsArray[0] = new HospitalRecord(101, "Sravan", 15);
        obj1.doctorsArray[1] = new HospitalRecord(102, "Jagan", 5);
        obj1.doctorsArray[2] = new HospitalRecord(103, "Maryam", 20);
        obj1.doctorCount = 3;

        boolean Continue = true;
        
        do {
            System.out.println("\n=== HOSPITAL DASHBOARD ===");
            System.out.println("1.  Add Patient to the waiting queue");
            System.out.println("2.  Admit next Patient from queue");
            System.out.println("3.  View waiting queue");
            System.out.println("4.  Show admitted patients list");
            System.out.println("5.  Delete Patient (Discharge)");
            System.out.println("6.  Add doctors");
            System.out.println("7.  Delete doctor");
            System.out.println("8.  Search Doctors");
            System.out.println("9.  Show Doctors");
            System.out.println("10. View Patient Visit History");
            System.out.println("11. Validate Prescription (Stack App)");
            System.out.println("12. Exit");
            System.out.print("Select Option: ");

            int choice = obj1.sc.nextInt();

            switch (choice) {
                case 1: obj1.addPatientToQueue(); break;
                case 2: obj1.admitPatientFromQueue(); break;
                case 3: obj1.displayQueue(); break;
                case 4: obj1.showPatients(); break;
                case 5: obj1.deletePatient(); break; 
                case 6: obj1.addDoctor(); break;
                case 7: obj1.deleteDoctor(); break;
                case 8: obj1.searchDoctor(); break;
                case 9: obj1.showDoctorsSorted(); break;
                case 10: obj1.viewHistory(); break;
                case 11: obj1.validatePrescription(); break;
                case 12: Continue = false; System.out.println("System Closed. Please be fit & Stay healthy!"); break;
                default: System.out.println("Invalid option.");
            }
        } while (Continue);
    }
}