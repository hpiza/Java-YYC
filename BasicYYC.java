import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class BasicYYC {
		
	public static void main(String[] args) throws Exception {
		boolean[][] bm = readBM("bol/mb40x42.bol");
		runYYC(bm);		
	}
	
	public static void runYYC(boolean[][] bm) {
		int cols = bm[0].length;
		int rows = bm.length;
		List<BitSet> Psi = new ArrayList<BitSet>();
			
		int[] onesCountPerRow = new int[rows];
		boolean[] isColumnMarked = new boolean[cols];
		
		for(int xj = 0; xj < cols; xj ++) {
			if(bm[0][xj]) Psi.add(createIT(cols, xj));
		}
		for(int ri = 1; ri < rows; ri ++) {
			List<BitSet> PsiAux = new ArrayList<BitSet>();
			for(BitSet tj : Psi) {
				int tjSize = 0;
				boolean found = false;
				for(int xp = 0; xp < cols; xp ++) {
					if(tj.get(xp)) {
						tjSize ++;
						if(bm[ri][xp]) found = true;
					}
				}
				if(found) {
					PsiAux.add(tj);
				} else {
					for(int xp = 0; xp < cols; xp ++) {
						if(!bm[ri][xp]) continue;
						if(findCompatibleSet(bm, tj, xp, ri, tjSize, isColumnMarked, onesCountPerRow)) {
							BitSet tjxp = (BitSet) tj.clone();
							tjxp.set(xp);
							PsiAux.add(tjxp);
						}
					}
				}
			}
			Psi = PsiAux;
//			System.out.println("Fila: " + ri + ": TI al momento: " + Psi.size());
		}
		//for(BitSet it : Psi) System.out.println(it);
		System.out.println("Número de TI encontrados: " + Psi.size());
	}
	
	private static boolean findCompatibleSet(boolean[][] bm, BitSet tao, 
											 int xp, int currentRow, int taoSize,
							                 boolean[] isColumnMarked, 
							                 int[] onesCountPerRow) {		
		for(int i = 0; i < isColumnMarked.length; i ++) isColumnMarked[i] = false;
		isColumnMarked[xp] = true;
		int cols = bm[0].length;	
		for(int c = 0; c < cols; c ++) if(tao.get(c)) isColumnMarked[c] = true;
		int unitaryRowsCount = 0;
		for(int rs = 0; rs <= currentRow; rs ++) {
			onesCountPerRow[rs] = 0;
			int sum = 0;
			for(int f = 0; f < cols && sum < 2; f ++) {
				if(!isColumnMarked[f]) continue;
				if(bm[rs][f]) {
					sum ++;
					onesCountPerRow[rs] ++;
				}
			}
			if(sum == 1) unitaryRowsCount ++;
		}
		if(unitaryRowsCount < taoSize + 1) return false;
		
		for(int xk = 0; xk < cols; xk ++) {
			if(!isColumnMarked[xk]) continue;
			int sumXk = 0;
			for(int rs = 0; rs <= currentRow; rs ++) {
				if(onesCountPerRow[rs] != 1) continue;
				if(bm[rs][xk]) sumXk ++;
			}
			if(sumXk < 1) return false;
		}
		return true;
	}
	
	private static BitSet createIT(int length, int feature) {
		BitSet bs = new BitSet(length);
		bs.set(feature);
		return bs;
	}

	public static boolean[][] readBM(String filename) throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		int rows    = Integer.parseInt(br.readLine().trim());
		int columns = Integer.parseInt(br.readLine().trim());
		boolean[][] bm = new boolean[rows][columns];
		for(int r = 0; r < rows; r ++) {
			String line = br.readLine().trim();
			String[] lineArray = line.split(" ");
			for(int c = 0; c < columns; c ++) {
				if(lineArray[c].equals("1")) bm[r][c] = true;
			}
		}
		br.close();
		return bm;
	}
	
}
