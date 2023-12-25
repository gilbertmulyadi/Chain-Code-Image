import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Chain {
    // Properti gambar
    int h; // tinggi
    int w; // lebar

    // Properti bentuk
    int height;
    int width;

    // Citra biner
    int pixels[][]; // gambar dengan ambang batas 1 atau 0
    int visited[][]; // menyimpan piksel yang sudah dikunjungi

    // Koordinat awal bentuk
    int begin[];

    // Koordinat akhir bentuk
    int end[];

    // Keliling
    int points;
    double perimeter;

    public Chain() throws IOException {

        // Baca file input
        System.out.print("Nama File: ");
        String filename = new String();
        filename = Input.readString();
        File shape = new File(filename);
        BufferedImage image = ImageIO.read(shape);

        // Atur properti gambar untuk digunakan nanti
        h = image.getHeight(); // tinggi
        w = image.getWidth(); // lebar

        // Inisialisasi vektor koordinat
        begin = new int[2];
        end = new int[2];
        points = 0;
        perimeter = 0;

        // Ambang citra
        pixels = new int[h][w];
        visited = new int[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                pixels[i][j] = image.getRGB(j, i);
                if (pixels[i][j] != -1) {
                    // variasi warna abu-abu -> hitam
                    pixels[i][j] = 1;
                } else {
                    // latar belakang -> putih
                    pixels[i][j] = 0;
                }
                // atur piksel sebagai belum dikunjungi
                visited[i][j] = 0;
            }
        }   
    }

    public void firstPixel() {
        boolean flag = false;
        // temukan piksel hitam pertama
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (pixels[i][j] == 1 && !(flag)) {
                    // dapatkan koordinat
                    begin[0] = i;
                    begin[1] = j;
                    flag = true;
                }
            }
        }
    }

    public void lastPixel() {
        boolean flag = false;
        // temukan piksel pertama dari bawah ke atas
        for (int i = h - 1; i >= 0; i--) {
            for (int j = w - 1; j >= 0; j--) {
                if (pixels[i][j] == 1 && !(flag)) {
                    // dapatkan koordinat
                    end[0] = i;
                    end[1] = j;
                    flag = true;
                }
            }
        }
    }

    public void setHeight() {
        // y dari piksel terakhir - y dari piksel pertama
        height = (end[0] - begin[0] + 1);                    
    }

    public void setWidth() {

        // dapatkan koordinat x piksel pertama dan terakhir
        int aux[] = new int[2];
        boolean flag = false;
        // temukan piksel pertama ke kiri
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (pixels[j][i] == 1 && !(flag)) {
                    // dapatkan koordinat x
                    aux[0] = i;
                    flag = true;
                }
            }
        }

        flag = false;
        // temukan piksel pertama ke kanan
        for (int i = w - 1; i >= 0; i--) {
            for (int j = h - 1; j >= 0; j--) {
                if (pixels[j][i] == 1 && !(flag)) {
                    // dapatkan koordinat x
                    aux[1] = i;
                    flag = true;
                }
            }
        }

        // x dari piksel terakhir - x dari piksel pertama
        width = (aux[1] - aux[0] + 1);

    }

    public void border() {

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (pixels[i][j] == 1) {
                    // jika tetangga dari suatu piksel kosong, piksel tersebut berada di pinggiran bentuk
                    if (borderPixel(i, j)) points++;
                }
            }
        }
    }

    public boolean borderPixel(int i, int j) {

        // hanya periksa piksel hitam
        if (pixels[i][j] == 0) return false;
        
        // periksa kiri
        if (j == 0) return true; // batas gambar = batas bentuk
        if (j > 0) {
            if (pixels[i][j - 1] == 0) {
                return true;
            }
        }

        // periksa atas
        if (i == 0) return true;
        if (i > 0) {
            if (pixels[i - 1][j] == 0) {
                return true;
            }
        }

        // periksa kanan
        if (j == w) return true;
        if (j < w) {
            if (pixels[i][j + 1] == 0) {
                return true;
            }
        }

        // periksa bawah
        if (i == h) return true;
        if (i < h) {
            if (pixels[i + 1][j] == 0) {
                return true;
            }
        }

        // tidak ada piksel kosong di sekitar = bukan piksel pinggiran
        return false;
    }

    public int[] borderNeighbors(int i, int j) {
        
        int index[] = new int[2];
        boolean flag = false;

        // periksa sekitar piksel untuk piksel pinggiran yang belum dikunjungi
        // menghitung jarak kode rantai

        // periksa timur
        if (borderPixel(i, j+1) && !flag && !flag && visited[i][j+1] == 0) {
            j = j + 1;
            System.out.print("0 ");
            perimeter += 1;
            flag = true;
            index[0] = i;
            index[1] = j;
            return index;
        }
        // periksa tenggara
        if (borderPixel(i+1, j+1) && !flag && visited[i+1][j+1] == 0) {
            i = i + 1;
            j = j + 1;
            System.out.print("1 ");
            perimeter += Math.sqrt(2);
            flag = true;
            index[0] = i;
            index[1] = j;
            return index;
        }
        // periksa selatan
        if (borderPixel(i+1, j) && !flag && visited[i+1][j] == 0) {
            i = i + 1;
            System.out.print("2 ");
            perimeter += 1;
            flag = true;
            index[0] = i;
            index[1] = j;
            return index;
        }
        // periksa barat daya
        if (borderPixel(i+1, j-1) && !flag && visited[i+1][j-1] == 0) {
            i = i + 1;
            j = j - 1;
            System.out.print("3 ");
            perimeter += Math.sqrt(2);
            flag = true;
            index[0] = i;
            index[1] = j;
            return index;
        }
        // periksa barat
        if (borderPixel(i, j-1) && !flag && visited[i][j-1] == 0) {
            j = j - 1;
            System.out.print("4 ");
            perimeter += 1;
            flag = true;
            index[0] = i;
            index[1] = j;
            return index;
        }
        // periksa barat laut
        if (borderPixel(i-1, j-1) && !flag && visited[i-1][j-1] == 0) {
            i = i - 1;
            j = j - 1;
            System.out.print("5 ");
            perimeter += Math.sqrt(2);
            flag = true;
            index[0] = i;
            index[1] = j;
            return index;
        }
        // periksa utara
        if (borderPixel(i-1, j) && !flag && visited[i-1][j] == 0) {
            i = i - 1;
            System.out.print("6 ");
            perimeter += 1;
            flag = true;
            index[0] = i;
            index[1] = j;
            return index;
        }
        // periksa timur laut
        if (borderPixel(i-1, j+1) && !flag && visited[i-1][j+1] == 0) {
            i = i - 1;
            j = j + 1;
            System.out.print("7 ");
            perimeter += Math.sqrt(2);
            flag = true;
            index[0] = i;
            index[1] = j;
            return index;
        }
        // tidak ada piksel pinggiran tetangga 
        index[0] = i;
        index[1] = j;
        return index;
    }

    public void chainCodes(int i, int j) {

        /*
        i dan j = indeks piksel saat ini
        index[0], index[1] = piksel pinggiran berikutnya (jika ada)
        */

        // koordinat piksel saat ini
        int[] index = new int[2];

        // periksa piksel pinggiran sekitar 
        index = borderNeighbors(i, j);

        // atur piksel sebagai sudah dikunjungi
        visited[i][j] = 1;

        // jika piksel pinggiran berikutnya sudah dikunjungi, kita kembali ke piksel pertama 
        if (visited[index[0]][index[1]] == 0) {
            chainCodes(index[0], index[1]);
        } else {
            System.out.println();
        }
    }

   public static void main(String[] args) throws IOException {

        Chain c = new Chain();

        // dapatkan koordinat kunci
        c.firstPixel();
        c.lastPixel();

        // hitung properti bentuk
        c.setHeight();
        c.setWidth();
        System.out.println("Lebar Bentuk: " + c.width);
        System.out.println("Tinggi Bentuk: " + c.height);
        
        // hasilkan kode rantai
        // dapatkan koordinat piksel pinggiran pertama setelah awal
        int[] index = new int[2];
        System.out.print("Kode Rantai: ");
        index = c.borderNeighbors(c.begin[0], c.begin[1]);
        c.chainCodes(index[0], index[1]);       

        // dapatkan ukuran keliling
        c.border();
        System.out.println("Piksel Pinggiran: " + c.points + " piksel");
        System.out.println("Shape Perimeter: " + c.perimeter);
    }
}
