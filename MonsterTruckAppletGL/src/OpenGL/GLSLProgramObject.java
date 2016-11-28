package OpenGL;

import com.jogamp.opengl.GL2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

// Translated from C++ Version see below:
//
// GLSLProgramObject.h - Wrapper for GLSL program objects
//
// Author: Louis Bavoil
// Email: sdkfeedback@nvidia.com
//
// Copyright (c) NVIDIA Corporation. All rights reserved.
////////////////////////////////////////////////////////////////////////////////

public class GLSLProgramObject {

//    protected Vector<Integer> _vertexShaders = new Vector<>();
//    protected Vector<Integer> _fragmentShaders = new Vector<>();
    protected ArrayList<Integer> _vertexShaders = new ArrayList<>();
    protected ArrayList<Integer> _fragmentShaders = new ArrayList<>();
    private Integer _progId;
//    private String shadersPath = "/shaders/";

    public GLSLProgramObject(GL2 gl2) {
        _progId = 0;
    }

    public GLSLProgramObject(GL2 gl2, String shadersFilepath, String vertexShader, String fragmentShader) {

        this(gl2);

        attachVertexShader(gl2, shadersFilepath + vertexShader);
        attachFragmentShader(gl2, shadersFilepath + fragmentShader);

        initializeProgram(gl2, true);
    }

    public GLSLProgramObject(GL2 gl2, String shadersFilepath, String[] vertexShaders, String[] fragmentShaders) {

        this(gl2);

        for (String vertexShader : vertexShaders) {
            attachVertexShader(gl2, shadersFilepath + vertexShader);
        }
        for (String fragmentShader : fragmentShaders) {
            attachFragmentShader(gl2, shadersFilepath + fragmentShader);
        }

        initializeProgram(gl2, true);
    }

    public void destroy(GL2 gl2) {
        for (int i = 0; i < _vertexShaders.size(); i++) {
            gl2.glDeleteShader(_vertexShaders.get(i));
        }
        for (int i = 0; i < _fragmentShaders.size(); i++) {
            gl2.glDeleteShader(_fragmentShaders.get(i));
        }
        if (_progId != 0) {
            gl2.glDeleteProgram(_progId);
        }
    }

    public void bind(GL2 gl2) {
        gl2.glUseProgram(_progId);
    }

    public void unbind(GL2 gl2) {
        gl2.glUseProgram(0);
    }

    public void setUniform(GL2 gl2, String name, float[] val, int count) {
        int id = gl2.glGetUniformLocation(_progId, name);
        if (id == -1) {
            System.err.println("Warning: Invalid uniform parameter " + name);
            return;
        }
        switch (count) {
            case 1:
                gl2.glUniform1fv(id, 1, val, 0);
                break;
            case 2:
                gl2.glUniform2fv(id, 1, val, 0);
                break;
            case 3:
                gl2.glUniform3fv(id, 1, val, 0);
                break;
            case 4:
                gl2.glUniform4fv(id, 1, val, 0);
                break;
        }
    }

    public void setTextureUnit(GL2 gl2, String texname, int texunit) {
        int[] params = new int[]{0};
        gl2.glGetProgramiv(_progId, gl2.GL_LINK_STATUS, params, 0);
        if (params[0] != 1) {
            System.err.println("Error: setTextureUnit needs program to be linked.");
        }
        int id = gl2.glGetUniformLocation(_progId, texname);
        if (id == -1) {
            System.err.println("Warning: Invalid texture " + texname);
            return;
        }
        gl2.glUniform1i(id, texunit);
    }

    public void bindTexture(GL2 gl2, int target, String texname, int texid, int texunit) {
        gl2.glActiveTexture(gl2.GL_TEXTURE0 + texunit);
        gl2.glBindTexture(target, texid);
        setTextureUnit(gl2, texname, texunit);
        gl2.glActiveTexture(gl2.GL_TEXTURE0);
    }

    public void bindTexture2D(GL2 gl2, String texname, int texid, int texunit) {
        bindTexture(gl2, gl2.GL_TEXTURE_2D, texname, texid, texunit);
    }

    public void bindTexture3D(GL2 gl2, String texname, int texid, int texunit) {
        bindTexture(gl2, gl2.GL_TEXTURE_3D, texname, texid, texunit);
    }

    public void bindTextureRECT(GL2 gl2, String texname, int texid, int texunit) {
        bindTexture(gl2, gl2.GL_TEXTURE_RECTANGLE, texname, texid, texunit);
    }

    public final void attachVertexShader(GL2 gl2, String filename) {
//        System.out.println("\nfilename " + filename);
//        URL fileURL = getClass().getClassLoader().getResource(
//                File.separator + "depthPeeling"
//                + File.separator + "shaders" + File.separator + filename);
//        String resourcePath = shadersPath + filename;
        String resourcePath = filename;
//        System.out.println("filename "+filename);
        URL fileURL = getClass().getResource(resourcePath);
        System.err.format(fileURL.toString());
        if (fileURL != null) {
            String content = "";
            BufferedReader input = null;

            try {
                input = new BufferedReader(new InputStreamReader(fileURL.openStream()));
                String line = null;

                while ((line = input.readLine()) != null) {
                    content += line + "\n";
                }
            } catch (FileNotFoundException fileNotFoundException) {
                System.err.println("Unable to find the shader file " + filename);
            } catch (IOException iOException) {
                System.err.println("Problem reading the shader file " + filename);
            } finally {
                try {
                    if (input != null) {
                        input.close();
                    }
                } catch (IOException iOException) {
                    System.out.println("Problem closing the BufferedReader, " + filename);
                }
            }

            int iID = gl2.glCreateShader(gl2.GL_VERTEX_SHADER);

            String[] akProgramText = new String[1];
            // find and replace program name with "main"
            akProgramText[0] = content;

            int[] params = new int[]{0};

            int[] aiLength = new int[1];
            aiLength[0] = akProgramText[0].length();
            int iCount = 1;

            gl2.glShaderSource(iID, iCount, akProgramText, aiLength, 0);

            gl2.glCompileShader(iID);

            gl2.glGetShaderiv(iID, gl2.GL_COMPILE_STATUS, params, 0);

            if (params[0] != 1) {
                System.err.println(filename);
                System.err.println("compile status: " + params[0]);
                gl2.glGetShaderiv(iID, gl2.GL_INFO_LOG_LENGTH, params, 0);
                System.err.println("log length: " + params[0]);
                byte[] abInfoLog = new byte[params[0]];
                gl2.glGetShaderInfoLog(iID, params[0], params, 0, abInfoLog, 0);
                System.err.println(new String(abInfoLog));
                System.exit(-1);
            }
            _vertexShaders.add(iID);
        } else {
            System.err.println("Unable to find the shader file " + filename);
        }
    }

    public final void attachFragmentShader(GL2 gl2, String filename) {
//        URL fileURL = getClass().getClassLoader().getResource(
//                File.separator + "depthPeeling"
//                + File.separator + "shaders" + File.separator + filename);
//        String resourcePath = shadersPath + filename;
        String resourcePath = filename;
        URL fileURL = getClass().getResource(resourcePath);
        if (fileURL != null) {
            String content = "";
            BufferedReader input = null;

            try {
                input = new BufferedReader(new InputStreamReader(fileURL.openStream()));
                String line = null;

                while ((line = input.readLine()) != null) {
                    content += line + "\n";
                }
            } catch (FileNotFoundException fileNotFoundException) {
                System.err.println("Unable to find the shader file " + filename);
            } catch (IOException iOException) {
                System.err.println("Problem reading the shader file " + filename);
            } finally {
                try {
                    if (input != null) {
                        input.close();
                    }
                } catch (IOException iOException) {
                    System.out.println("Problem closing the BufferedReader, " + filename);
                }
            }

            int iID = gl2.glCreateShader(gl2.GL_FRAGMENT_SHADER);

            String[] akProgramText = new String[1];
            // find and replace program name with "main"
            akProgramText[0] = content;

            int[] params = new int[]{0};

            int[] aiLength = new int[1];
            aiLength[0] = akProgramText[0].length();
            int iCount = 1;

            gl2.glShaderSource(iID, iCount, akProgramText, aiLength, 0);

            gl2.glCompileShader(iID);

            gl2.glGetShaderiv(iID, gl2.GL_COMPILE_STATUS, params, 0);

            if (params[0] != 1) {
                System.err.println(filename);
                System.err.println("compile status: " + params[0]);
                gl2.glGetShaderiv(iID, gl2.GL_INFO_LOG_LENGTH, params, 0);
                System.err.println("log length: " + params[0]);
                byte[] abInfoLog = new byte[params[0]];
                gl2.glGetShaderInfoLog(iID, params[0], params, 0, abInfoLog, 0);
                System.err.println(new String(abInfoLog));
                System.exit(-1);
            }
            _fragmentShaders.add(iID);
        } else {
            System.err.println("Unable to find the shader file " + filename);
        }
    }

    public final void initializeProgram(GL2 gl2, boolean cleanUp) {
        _progId = gl2.glCreateProgram();

        for (int i = 0; i < _vertexShaders.size(); i++) {
            gl2.glAttachShader(_progId, _vertexShaders.get(i));
        }

        for (int i = 0; i < _fragmentShaders.size(); i++) {
            gl2.glAttachShader(_progId, _fragmentShaders.get(i));
        }

        gl2.glLinkProgram(_progId);

        int[] params = new int[]{0};
        gl2.glGetProgramiv(_progId, gl2.GL_LINK_STATUS, params, 0);

        if (params[0] != 1) {

            System.err.println("link status: " + params[0]);
            gl2.glGetProgramiv(_progId, gl2.GL_INFO_LOG_LENGTH, params, 0);
            System.err.println("log length: " + params[0]);

            byte[] abInfoLog = new byte[params[0]];
            gl2.glGetProgramInfoLog(_progId, params[0], params, 0, abInfoLog, 0);
            System.err.println(new String(abInfoLog));
        }

        gl2.glValidateProgram(_progId);

        if (cleanUp) {
            for (int i = 0; i < _vertexShaders.size(); i++) {
                gl2.glDetachShader(_progId, _vertexShaders.get(i));
                gl2.glDeleteShader(_vertexShaders.get(i));
            }

            for (int i = 0; i < _fragmentShaders.size(); i++) {
                gl2.glDetachShader(_progId, _fragmentShaders.get(i));
                gl2.glDeleteShader(_fragmentShaders.get(i));
            }
        }
    }

    public Integer getProgramId() {
        return _progId;
    }
};
