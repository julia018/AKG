import logic.Phong;
import logic.Texture;
import logic.Transformation;
import model.Triangle;
import model.Vector2;
import model.Vector3;

import java.awt.*;

public class Bresenhime {
    // Этот код "рисует" все 9 видов отрезков. Наклонные (из начала в конец и из конца в начало каждый), вертикальный и горизонтальный - тоже из начала в конец и из конца в начало, и точку.
    private static int sign(int x) {
        return (x > 0) ? 1 : (x < 0) ? -1 : 0;
        //возвращает 0, если аргумент (x) равен нулю; -1, если x < 0 и 1, если x > 0.
    }

    public static void drawBresenhamLine(Triangle tr, Texture texture, int xstart, int ystart, float zStart, float zEnd, int xend, int yend, Display d, float[] zBuffer, Vector3 startNormal, Vector3 endNormal, Vector2 uvStart, Vector2 uvEnd)
    /**
     * xstart, ystart - начало;
     * xend, yend - конец;
     * "g.drawLine (x, y, x, y);" используем в качестве "setPixel (x, y);"
     * Можно писать что-нибудь вроде g.fillRect (x, y, 1, 1);
     */
    {

        Vector3 normalStart = startNormal;
        Vector3 normalEnd = endNormal;
        Vector2 uv;
        float uu;
        float v;
        Color colorSpec;
        int x, y, dx, dy, incx, incy, incz, pdx, pdy, es, el, err;
        float dz, zStep, z, u;
        Vector3 lightnew;
        float curW = 0;
        //g.setColor(Color.BLACK);
        dx = xend - xstart;//проекция на ось икс
        dy = yend - ystart;//проекция на ось игрек
        dz = zEnd - zStart;
        int dn = 0;

        if(dz <= 0) {
            incz = -1;
        } else {
            incz = 1;
        }

        dz = Math.abs(dz);

        incx = sign(dx);
        /*
         * Определяем, в какую сторону нужно будет сдвигаться. Если dx < 0, т.е. отрезок идёт
         * справа налево по иксу, то incx будет равен -1.
         * Это будет использоваться в цикле постороения.
         */
        incy = sign(dy);
        /*
         * Аналогично. Если рисуем отрезок снизу вверх -
         * это будет отрицательный сдвиг для y (иначе - положительный).
         */

        if (dx < 0) dx = -dx;//далее мы будем сравнивать: "if (dx < dy)"
        if (dy < 0) dy = -dy;//поэтому необходимо сделать dx = |dx|; dy = |dy|
        //эти две строчки можно записать и так: dx = Math.abs(dx); dy = Math.abs(dy);

        if (dx > dy)
        //определяем наклон отрезка:
        {
            /*
             * Если dx > dy, то значит отрезок "вытянут" вдоль оси икс, т.е. он скорее длинный, чем высокий.
             * Значит в цикле нужно будет идти по икс (строчка el = dx;), значит "протягивать" прямую по иксу
             * надо в соответствии с тем, слева направо и справа налево она идёт (pdx = incx;), при этом
             * по y сдвиг такой отсутствует.
             */
            pdx = incx;
            pdy = 0;
            es = dy;
            el = dx;
            zStep = Math.abs(dz / dx);
            dn = dx;
        } else//случай, когда прямая скорее "высокая", чем длинная, т.е. вытянута по оси y
        {
            pdx = 0;
            pdy = incy;
            es = dx;
            el = dy;//тогда в цикле будем двигаться по y
            zStep = Math.abs(dz / dy);
            dn = dy;
        }
        x = xstart;
        y = ystart;
        z = zStart;
        //float cos = cos(new Vector3(xstart, ystart, zStart), inversedProject.multiplyByVector(normalStart), lightSource);
        err = el / 2;
        //color = phong.getResultPhongColor(vectorObserver, tr.getInterpolatedNormal(xstart,ystart).getNormalized(), eyePoint, proj, vp);
        //colorSpec = tr.getTextureColor(texture);
        colorSpec = texture.getSpecularColor(uvStart);
        d.drawPixel(x, y, zStart, zBuffer, (byte)255, (byte) colorSpec.getBlue(), (byte) colorSpec.getGreen(), (byte) colorSpec.getRed());//ставим первую точку
        //все последующие точки возможно надо сдвигать, поэтому первую ставим вне цикла

        for (int t = 0; t < el; t++)//идём по всем точкам, начиная со второй и до последней
        {
            err -= es;
            if (err < 0) {
                err += el;
                x += incx;//сдвинуть прямую (сместить вверх или вниз, если цикл проходит по иксам)
                y += incy;//или сместить влево-вправо, если цикл проходит по y

            } else {
                x += pdx;//продолжить тянуть прямую дальше, т.е. сдвинуть влево или вправо, если
                y += pdy;//цикл идёт по иксу; сдвинуть вверх или вниз, если по y
            }
            if(pdx != 0) {
                u = (x - xstart) / (float)dx;
            } else {
                u = (y - ystart) / (float)dy;
            }
            z = tr.getInterpolatedZ(x, y);

            float upU = (1 - u)*uvStart.getX()/zStart + u * uvEnd.getX()/zEnd;
            float downU = (1 - u)*1f/zStart + u * 1f/zEnd;
            float resU = upU / downU;
            float upV = (1 - u)*uvStart.getY()/zStart + u * uvEnd.getY()/zEnd;
            float resV = upV / downU;
            uv = new Vector2(resU, resV);
            //color = phong.getResultPhongColor(vectorObserver1, newNormal, eyePoint, proj, vp);
            //colorSpec = tr.getTextureColor(texture);
            colorSpec = texture.getSpecularColor(uv);
            d.drawPixel(x, y, z, zBuffer, (byte)255, (byte) colorSpec.getBlue(), (byte) colorSpec.getGreen(), (byte) colorSpec.getRed());
        }

    }

    private static float cos(Vector3 vector, Vector3 normal, Vector3 lightSource) {
        Vector3 light = vector.substractVector(lightSource).getNormalized();
        Vector3 newLight = new Vector3(light.getX() * -1, light.getY() * -1, light.getZ() * -1);
        return Math.max(0, normal.getScalarProduct(newLight));
    }
}
