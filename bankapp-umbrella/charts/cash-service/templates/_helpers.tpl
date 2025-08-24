cl{{- define "cash-service.name" -}}
{{ .Chart.Name }}
{{- end }}

{{- define "cash-service.fullname" -}}
{{ .Release.Name }}-{{ .Chart.Name }}
{{- end }}

{{- define "cash-service.selectorLabels" -}}
app: {{ include "cash-service.name" . }}
{{- end }}

{{- define "cash-service.labels" -}}
app: {{ include "cash-service.name" . }}
chart: {{ .Chart.Name }}-{{ .Chart.Version }}
release: {{ .Release.Name }}
heritage: {{ .Release.Service }}
{{- end }}