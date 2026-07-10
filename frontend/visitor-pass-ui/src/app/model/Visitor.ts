export interface Visitor {
    id?: string;
    residentId?: string;
    visitorName: string;
    visitorPhone: string;
    visitorEmail: string;
    purpose: string;
    visitDate: string;
    status?: string;
    hasPass?: boolean;
}