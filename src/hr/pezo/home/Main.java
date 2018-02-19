package hr.pezo.home;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class Main {
	private static File tags = new File("data/tags.txt");
	private static File data = new File("data/data.csv");
	
	public static void main(String[] args) throws IOException {
		List<BigInteger> tagovi = new ArrayList<>();
		List<String> lista = new ArrayList<>();
		List<String> csvLista = new ArrayList<>();
		
		lista = Files.readAllLines(tags.toPath());
		
		//Ucitava sve proizvode
		for(String s : lista) {
			if(s.matches("-?[0-9a-fA-F]+")) {
				if(!s.startsWith("3")) {
					System.out.println("this one has no valid header: " +s);
				}else {
					tagovi.add(new BigInteger(s,16));
				}
			}else {
				System.out.println("this one is not hexadecimal: " +s);
			}
		}
		
		//Cita item reference iz csv datoteke
		BigInteger itemReference = null;
		csvLista = Files.readAllLines(data.toPath());
		for(String s:csvLista) {
			if(s.contains("Milka Oreo")) {
				String[] oreoLinija = s.split(";");
				itemReference = new BigInteger(oreoLinija[2],10);
			}
		}
		
		//Ispisuje serijske brojeve i broji proizvode koji imaju odgovarajucu item reference
		int count=0;
		System.out.println("\nSerial numbers of oreo cokolade:\n");
		for(BigInteger b : tagovi) {
			int ItemReferenceOffset = Integer.parseInt(b.toString(2).substring(9,12),2);
			if(ItemReferenceOffset>6) {
				System.out.println("this one has invalid partition value: " +b.toString(16));
				continue;
			}
			else if(ItemReferenceOffset==1)ItemReferenceOffset=3;
			else if(ItemReferenceOffset==2)ItemReferenceOffset=6;
			else if(ItemReferenceOffset==3)ItemReferenceOffset=10;
			else if(ItemReferenceOffset==4)ItemReferenceOffset=13;
			else if(ItemReferenceOffset==5)ItemReferenceOffset=16;
			else if(ItemReferenceOffset==6)ItemReferenceOffset=20;
			
			
			if(b.toString(2).substring(52-ItemReferenceOffset, b.toString(2).length()-38).endsWith(itemReference.toString(2))) {
				System.out.println(b.toString(2).substring(b.toString(2).length()-38));
				count++;
			}
		}
		System.out.println("\nThis makes total of " +count +"pcs of milka oreo cokolada");
	}

}
